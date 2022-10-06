package com.example.messagingextension.channel;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublisherChannel {

    private String topic = "usage-threshold-80pct";

    @Bean
    @ServiceActivator(inputChannel = "myOutputChannel")
    public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
        log.info("Publishing message for {} topic", this.topic);
        PubSubMessageHandler adapter = new PubSubMessageHandler(pubsubTemplate, this.topic);
        adapter.setSuccessCallback(
                ((ackId, message) ->
                        log.info("Message was sent via the outbound channel adapter to usage-threshold-80pct!")));
        adapter.setFailureCallback(
                (cause, message) -> log.info("Error sending " + message + " due to " + cause));
        return adapter;
    }

    @MessagingGateway(defaultRequestChannel = "myOutputChannel")
    public interface PubsubOutboundGateway {
        void sendToPubSub(String text);
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
