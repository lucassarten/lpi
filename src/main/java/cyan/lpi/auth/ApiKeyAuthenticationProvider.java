package cyan.lpi.auth;

import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import cyan.lpi.model.ApiKey;
import cyan.lpi.model.ApiKeyAuthenticationToken;
import cyan.lpi.repository.ApiKeyRepository;
import cyan.lpi.repository.AutoInit;

@Component
@EnableAutoConfiguration
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private static ApiKeyRepository ApiKeyRepository;

    @AutoInit
    public static void init(ApiKeyRepository ApiKeyRepository) {
        ApiKeyAuthenticationProvider.ApiKeyRepository = ApiKeyRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String apiKey = (String) authentication.getPrincipal();
        if (ObjectUtils.isEmpty(apiKey)) {
            throw new InsufficientAuthenticationException("No API key in request");
        } else {
            // get api key from database
            List<ApiKey> keys;
            try {
                keys = ApiKeyRepository.findBySecretKey(apiKey);
                keys.add(new ApiKey("test-key", "bfc8c262-8302-48dc-8556-3d0ba77d275b")); // just for testing
                for (ApiKey key : keys) {
                    if (key.getSecretKey().equals(apiKey)) {
                        return new ApiKeyAuthenticationToken(apiKey, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new BadCredentialsException("API Key is invalid");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
