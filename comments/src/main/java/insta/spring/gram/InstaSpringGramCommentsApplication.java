package insta.spring.gram;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;

@SpringCloudApplication
public class InstaSpringGramCommentsApplication {
    private static String key;

    public static void main(String[] args) {
        ConfigurableApplicationContext arr = SpringApplication.run(InstaSpringGramCommentsApplication.class, args);
    }

    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
