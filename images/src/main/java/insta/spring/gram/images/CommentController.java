
package insta.spring.gram.images;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@EnableBinding(Source.class)
public class CommentController {

    private final MeterRegistry meterRegistry;
    private FluxSink<Message<Comment>> commentSink;
    private Flux<Message<Comment>> flux;

    public CommentController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.flux = Flux.<Message<Comment>>create(
                emitter -> this.commentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }

    @PostMapping("/comments")
    public Mono<ResponseEntity<?>> addComment(Mono<Comment> newComment) {
        if (commentSink != null) {
            return newComment
                    .map(comment -> {commentSink.next(MessageBuilder
                            .withPayload(comment)
                            .setHeader(MessageHeaders.CONTENT_TYPE,
                                    MediaType.APPLICATION_JSON_VALUE)
                            .build());
                    return comment;
        })
                    .flatMap(comment -> {
                       meterRegistry
                       .counter("comments.produced", "imageId", comment.getImageId())
                       .increment();
                       return Mono.just(ResponseEntity.noContent().build());
                    });
        } else {
            return Mono.just(ResponseEntity.noContent().build());
        }
    }

    @StreamEmitter
    public void emit(@Output(Source.OUTPUT) FluxSender output) {
        output.send(this.flux);
    }

}
