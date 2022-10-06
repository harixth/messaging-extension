package com.example.messagingextension.channel;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReceiverChannel {

    @Bean
    public MessageChannel inputMessageChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter inboundChannelAdapter(
            @Qualifier("inputMessageChannel") MessageChannel messageChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate,
                        "usage-threshold-80pct-subscription");
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "inputMessageChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            log.info("Message arrived! Payload: " + new String((byte[]) message.getPayload()));
            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
            originalMessage.ack();
        };
    }
}
