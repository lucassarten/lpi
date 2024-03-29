package cyan.lpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "cyan.lpi.*")
@EnableJpaRepositories("cyan.lpi.repository")
public class LpiApplication {

	/**
	 * Entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(LpiApplication.class, args);
	}

}
