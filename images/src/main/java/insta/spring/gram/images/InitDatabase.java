package insta.spring.gram.images;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
public class InitDatabase {
    @Bean
	CommandLineRunner init(MongoOperations operations) {
        return args -> {
            operations.dropCollection(Image.class);

            operations.insert(new Image("1",
                    "photo1.jpg"));
            operations.insert(new Image("2",
                    "photo2.jpg"));
            operations.insert(new Image("3",
                    "photo3.png"));

            operations.findAll(Image.class).forEach(image -> {
                System.out.println(image.toString());
            });
        };
    }
}