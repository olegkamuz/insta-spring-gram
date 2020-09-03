package insta.spring.gram.comments;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class CommentController {

    private final CommentRepository repository;

    public CommentController(CommentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/comments/{imageId}")
    public Flux<Comment> comment(@PathVariable String imageId) {
        return repository.findByImageId(imageId);
    }

}
