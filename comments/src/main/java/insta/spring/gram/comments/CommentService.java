package insta.spring.gram.comments;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.converter.CompositeMessageConverterFactory;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@EnableBinding(Processor.class)
public class CommentService {

	private final CommentWriterRepository repository;

	private final MeterRegistry meterRegistry;
	private final static Logger log = LoggerFactory.getLogger(CommentService.class);


	public CommentService(CommentWriterRepository repository,
						  MeterRegistry meterRegistry) {
		this.repository = repository;
		this.meterRegistry = meterRegistry;
	}

    @StreamListener
    @Output(Processor.OUTPUT)
    public Flux<Comment> save(@Input(Processor.INPUT) Flux<Comment> newComments) {
        return repository
                .saveAll(newComments)
                .map(comment -> {
                    log.info("Saving new comment " + comment);
                    meterRegistry
                            .counter("comments.consumed", "imageId", comment.getImageId())
                            .increment();
                    return comment;
                });
    }

    @Bean
    public CompositeMessageConverterFactory mappingCompositeMessageConverterFactory() {
        return new CompositeMessageConverterFactory();
    }

	@Bean
	CommandLineRunner setUp(CommentWriterRepository repository) {
		return args -> {
			repository.deleteAll().subscribe();
		};
	}

}
