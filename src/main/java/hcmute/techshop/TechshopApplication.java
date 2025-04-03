package hcmute.techshop;

import hcmute.techshop.Config.DotEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hcmute.techshop")
public class TechshopApplication {

	public static void main(String[] args) {
		DotEnvConfig.loadEnv();
		SpringApplication.run(TechshopApplication.class, args);
	}

}
