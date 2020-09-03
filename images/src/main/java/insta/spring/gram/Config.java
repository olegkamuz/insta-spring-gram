package insta.spring.gram;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.converter.CompositeMessageConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CompositeMessageConverterFactory compositeMessageConverterFactory() {
        return new CompositeMessageConverterFactory();
    }
}