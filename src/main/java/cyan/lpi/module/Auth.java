package cyan.lpi.module;

import java.util.Map;

@ModuleDef(desc = "authentication management")
public class Auth implements Module {
    @CommandDef(desc = "authenticate with an api key", params = { "<api_key>" })
    public static String login(Map<String, String> params) {
        String api_key = params.get("0");
        // TODO: check api key
        return "Authentication successful";
    }
}
