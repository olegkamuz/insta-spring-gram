package insta.spring.gram;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import insta.spring.gram.images.ImageRepository;
import insta.spring.gram.images.Comment;
import insta.spring.gram.images.CommentController;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.support.BindingAwareModelMap;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("simulator")
@Component
public class CommentSimulator {

	private final CommentController controller;
	private final ImageRepository repository;

	private final AtomicInteger counter;
	private final HomeController homeController;

	public CommentSimulator(CommentController controller,
                            ImageRepository repository, HomeController homeController) {
		this.controller = controller;
		this.repository = repository;
        this.homeController = homeController;
        this.counter = new AtomicInteger(1);
	}

	@EventListener
	public void simulateComments(ApplicationReadyEvent event) throws InterruptedException {
		Disposable disposable =
                Flux
			.interval(Duration.ofMillis(1000))
			.flatMap(tick -> repository.findAll())
			.map(image -> {
				Comment comment = new Comment();
				comment.setImageId(image.getId());
				comment.setComment(
					"Comment #" + counter.getAndIncrement());
				return Mono.just(comment);
			})
			.flatMap(newComment ->
				Mono.defer(() ->
					controller.addComment(newComment)))
			.subscribe();

		Thread.sleep(5000000);
		disposable.dispose();
	}

	@EventListener
    public void simulateUsersClicking(ApplicationReadyEvent event) {
	    Flux
                .interval(Duration.ofMillis(500))
                .flatMap(tick ->
                        Mono.defer(() -> homeController.index(new BindingAwareModelMap())))
                .subscribe();
    }

}
