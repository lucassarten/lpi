package cyan.lpi.module;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import cyan.lpi.model.ApiKey;
import cyan.lpi.repository.ApiKeyRepository;
import cyan.lpi.repository.AutoInit;

/**
 * The Weather module provides utilities for getting weather forecasts.
 */
@ModuleDef(desc = "utilities for getting weather forecasts")
public class Weather implements Module {
    private static ApiKeyRepository ApiKeyRepository;

    @AutoInit
    public static void init(ApiKeyRepository ApiKeyRepository) {
        Weather.ApiKeyRepository = ApiKeyRepository;
    }

    /**
     * Get the current weather forecast for a location, defaults to Auckland
     * 
     * @param params
     * @return
     */
    @CommandDef(desc = "get the current weather forecast")
    public static String now(Map<String, String> params, Map<String, String> headers) {
        // Wellington New Zealand
        String lat = "-41.2529601";
        String lon = "174.7542577";
        // get open weather api key from database
        List<ApiKey> api_keys = ApiKeyRepository.findByApiKey("open-weather-map");
        if (api_keys.size() == 0) {
            return "No api key found for open-weather-map";
        }
        String api_key = api_keys.get(0).getSecretKey();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                            lat, lon, api_key)))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return "Failed to fetch weather data";
            }
            // Parse json response
            JSONObject jsonObject = new JSONObject(response.body());
            String condition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            BigDecimal temp = jsonObject.getJSONObject("main").getBigDecimal("temp");
            BigDecimal feels_like = jsonObject.getJSONObject("main").getBigDecimal("feels_like");
            BigDecimal humidity = jsonObject.getJSONObject("main").getBigDecimal("humidity");
            BigDecimal wind_speed = jsonObject.getJSONObject("wind").getBigDecimal("speed");
            BigDecimal rain_1h = new BigDecimal(0);
            if (!jsonObject.isNull("rain")) {
                rain_1h = jsonObject.getJSONObject("rain").getBigDecimal("1h");
            }
            // Convert temp from kelvin to celsius
            double temp_c = temp.doubleValue() - 273.15;
            double feels_like_c = feels_like.doubleValue() - 273.15;
            // Convert wind speed from km/h to m/s
            double wind_speed_ms = wind_speed.doubleValue() * 0.277778;
            // Format
            return String.format(
                    "%s - %.2f°C\n%smm rain (1h) %.2fm/s winds %s%% humidity",
                    condition, temp_c, rain_1h, wind_speed_ms, humidity);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Failed to fetch weather data";
    }
}
