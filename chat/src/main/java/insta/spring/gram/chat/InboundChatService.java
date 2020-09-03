package insta.spring.gram.chat;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(ChatServiceStreams.class)
public class InboundChatService extends UserParsingHandshakeHandler {
    private final ChatServiceStreams chatServiceStreams;

    public InboundChatService(ChatServiceStreams chatServiceStreams) {
        this.chatServiceStreams = chatServiceStreams;
    }

    @Override
    protected Mono<Void> handleInternal(WebSocketSession session) {
        return session
                .receive()
                .log(getUser(session.getId())
                        + "-inbound-incoming-chat-message")
                .map(WebSocketMessage::getPayloadAsText)
                .log(getUser(session.getId())
                        + "-inbound-convert-to-text")
                .flatMap(message ->
                        broadcast(message, getUser(session.getId())))
                .log(getUser(session.getId())
                        + "-inbound-broadcast-to-broker")
                .then();
    }

    public Mono<?> broadcast(String message, String user) {
        return Mono.fromRunnable(() -> {
            chatServiceStreams.clientToBroker().send(
                    MessageBuilder
                            .withPayload(message)
                            .setHeader(ChatServiceStreams.USER_HEADER, user)
                            .build());
        });
    }

}