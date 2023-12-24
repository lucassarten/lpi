package cyan.lpi.auth;

import cyan.lpi.model.ApiKeyAuthenticationToken;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiKeyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public ApiKeyAuthenticationFilter(AuthenticationManager authenticationManager) {
        super("/api/**");
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {

        Optional<String> apiKeyOptional = Optional.ofNullable(request.getHeader("x-api-key"));

        if (!apiKeyOptional.isPresent()) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
                    if (cookie.getName().equals("x-api-key")) {
                        apiKeyOptional = Optional.ofNullable(cookie.getValue());
                    }
                }
            }
        }

        ApiKeyAuthenticationToken token = apiKeyOptional.map(ApiKeyAuthenticationToken::new)
                .orElse(new ApiKeyAuthenticationToken());

        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
