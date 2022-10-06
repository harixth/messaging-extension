package com.example.messagingextension.channel;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PublisherChannel {

    // Create an outbound channel adapter to send messages from the input message channel to the
    // topic `topic-two`.
    @Bean
    @ServiceActivator(inputChannel = "myOutputChannel")
    public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
        PubSubMessageHandler adapter = new PubSubMessageHandler(pubsubTemplate, "topic-two");

        adapter.setSuccessCallback(
                ((ackId, message) ->
                        log.info("Message was sent via the outbound channel adapter to topic-two!")));

        adapter.setFailureCallback(
                (cause, message) -> log.info("Error sending " + message + " due to " + cause));

        return adapter;
    }

    @MessagingGateway(defaultRequestChannel = "myOutputChannel")
    public interface PubsubOutboundGateway {
        void sendToPubsub(String text);
    }
}
