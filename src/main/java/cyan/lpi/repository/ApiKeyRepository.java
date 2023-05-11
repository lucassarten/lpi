package cyan.lpi.repository;

import cyan.lpi.model.ApiKey;

import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;

@EnableAutoConfiguration
public interface ApiKeyRepository extends CrudRepository<ApiKey, Integer> {

    List<ApiKey> findBySecretKey(String SecretKey) throws Exception;

    List<ApiKey> findByApiKey(String ApiKey);

}
