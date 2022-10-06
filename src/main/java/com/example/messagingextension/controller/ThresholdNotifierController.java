package com.example.messagingextension.controller;

import com.example.messagingextension.channel.PublisherChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ThresholdNotifierController {

    @Autowired
    private PublisherChannel.PubsubOutboundGateway messagingGateway;

    @PostMapping("/messaging/publish")
    public String publishMessage(@RequestParam("message") String message) {
        messagingGateway.sendToPubsub(message);
        return "Message Sent";
    }
}
