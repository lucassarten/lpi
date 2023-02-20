package cyan.lpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
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
