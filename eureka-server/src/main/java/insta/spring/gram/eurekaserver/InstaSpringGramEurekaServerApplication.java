package insta.spring.gram.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class InstaSpringGramEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstaSpringGramEurekaServerApplication.class, args);
	}

}
