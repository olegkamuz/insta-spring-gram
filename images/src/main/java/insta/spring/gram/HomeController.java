package insta.spring.gram;

import java.util.HashMap;

import insta.spring.gram.images.CommentHelper;
import insta.spring.gram.images.ImageRepository;
import insta.spring.gram.images.ImageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableMongoRepositories(basePackageClasses = MongoOperations.class)
@ConfigurationProperties(prefix = "secret")
public class HomeController {
    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;
    private final CommentHelper commentHelper;


    public HomeController(ImageService imageService, ImageRepository imageRepository, Props props, CommentHelper commentHelper) {
        this.imageService = imageService;
        this.commentHelper = commentHelper;
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService
                .findAllImages()
                .map(image ->
                        new HashMap<String, Object>() {
                            {
                                put("id", image.getId());
                                put("name", image.getName());
                                put("comments", commentHelper.getComments(image));
                            }}
                            ));
        return Mono.just("index");
    }

}