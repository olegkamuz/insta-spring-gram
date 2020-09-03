package insta.spring.gram.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class LearningSpringBootHystrixDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningSpringBootHystrixDashboardApplication.class, args);
	}

}
