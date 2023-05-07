package cyan.lpi.repository;

import cyan.lpi.model.ApiKey;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ApiKeyRepository extends CrudRepository<ApiKey, Integer> {

    List<ApiKey> findBySecretKey(String SecretKey);

    List<ApiKey> findByApiKey(String ApiKey);

}
