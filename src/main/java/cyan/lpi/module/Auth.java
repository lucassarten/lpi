package cyan.lpi.module;

import java.util.List;
import java.util.Map;

import cyan.lpi.model.ApiKey;
import cyan.lpi.repository.ApiKeyRepository;

@ModuleDef(desc = "authentication management")
public class Auth implements Module {
    private static ApiKeyRepository ApiKeyRepository;

    public static void init(ApiKeyRepository ApiKeyRepository) {
        Auth.ApiKeyRepository = ApiKeyRepository;
    }

    @CommandDef(desc = "authenticate with an api key", params = { "<secret_key>" })
    public static String login(Map<String, String> params, Map<String, String> headers) throws Exception {
        String secret_key = params.get("0");
        // get api key from database
        List<ApiKey> keys = ApiKeyRepository.findBySecretKey(secret_key);
        // check if secret key matches, we could have multiple
        for (ApiKey key : keys) {
            if (key.getSecretKey().equals(secret_key)) {
                return "Authentication successful";
            }
        }

        return "Authentication failed";
    }
}
