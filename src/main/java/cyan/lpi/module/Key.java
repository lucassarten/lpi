package cyan.lpi.module;

import java.util.Map;

import org.springframework.stereotype.Service;

import cyan.lpi.model.ApiKey;
import cyan.lpi.repository.ApiKeyRepository;

@ModuleDef(desc = "api key management")
@Service
public final class Key implements Module {
    private static ApiKeyRepository ApiKeyRepository;

    public static void init(ApiKeyRepository ApiKeyRepository) {
        Key.ApiKeyRepository = ApiKeyRepository;
    }

    @CommandDef(desc = "add a new api key", params = { "<name>", "<secret_key>" })
    public static String add(Map<String, String> params, Map<String, String> headers) {
        String api_key = params.get("0");
        String secret_key = params.get("1");
        ApiKeyRepository.save(new ApiKey(api_key, secret_key));
        return "Key added";
    }

    @CommandDef(desc = "list all api keys")
    public static String list(Map<String, String> params, Map<String, String> headers) {
        // get key in use
        String api_key = headers.get("x-api-key");
        StringBuilder sb = new StringBuilder();
        for (ApiKey key : ApiKeyRepository.findAll()) {
            sb.append(String.format("%s %s %s %s\n", key.getId(), key.getApiKey(), key.getSecretKey(),
                    (key.getSecretKey().equals(api_key) ? "(in use)" : "")));
        }
        // remove trailing newline
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @CommandDef(desc = "delete an api key", params = { "<id>" })
    public static String delete(Map<String, String> params, Map<String, String> headers) {
        String id = params.get("0");
        ApiKeyRepository.deleteById(Integer.parseInt(id));
        return "Key deleted";
    }
}
