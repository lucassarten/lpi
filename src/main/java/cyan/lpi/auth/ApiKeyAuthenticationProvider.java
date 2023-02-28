package cyan.lpi.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import cyan.lpi.model.ApiKeyAuthenticationToken;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) {
        String apiKey = (String) authentication.getPrincipal();
        System.out.println("API Key: " + apiKey);
        if (ObjectUtils.isEmpty(apiKey)) {
            System.out.println("No API key in request");
            throw new InsufficientAuthenticationException("No API key in request");
        } else {
            if ("ValidApiKey".equals(apiKey)) {
                return new ApiKeyAuthenticationToken(apiKey, true);
            }
            System.out.println("API Key is invalid");
            throw new BadCredentialsException("API Key is invalid");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
