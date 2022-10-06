package com.example.messagingextension.controller;

import com.example.messagingextension.channel.PublisherChannel;
import com.example.messagingextension.dto.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ThresholdNotifierController {

    @Autowired
    private PublisherChannel.PubsubOutboundGateway messagingGateway;

    @Autowired
    private PublisherChannel publisherChannel;

    @PostMapping("/threshold-notifier/publish/{topic}")
    public String publishMessage(@RequestBody NotificationMessage body,
                                 @PathVariable String topic) {
        publisherChannel.setTopic(topic);
        messagingGateway.sendToPubSub(body.getMsg());
        return "Message Sent";
    }
}
