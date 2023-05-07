package cyan.lpi.repository;

import cyan.lpi.auth.ApiKeyAuthenticationProvider;
import cyan.lpi.module.Auth;
import cyan.lpi.module.Key;
import cyan.lpi.service.Service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@PropertySource(value = "classpath:application.properties")
@EnableAutoConfiguration
public class RepositoryContextConfiguration {
    @Autowired
    private ApiKeyRepository ApiKeyRepository;

    @Bean
    Service Service() {
        return new Service();
    }

    @PostConstruct
    public void init() {
        Key.init(ApiKeyRepository);
        Auth.init(ApiKeyRepository);
        ApiKeyAuthenticationProvider.init(ApiKeyRepository);
    }
}
