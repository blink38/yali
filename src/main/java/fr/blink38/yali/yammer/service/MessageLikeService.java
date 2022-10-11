package fr.blink38.yali.yammer.service;

import org.springframework.stereotype.Service;

import fr.blink38.yali.yammer.service.yammer.YammerService;

@Service
public class MessageLikeService extends YammerService<String> {

    @Override
    public String getUri() {
        return "/messages/liked_by/current.json";
    }
}