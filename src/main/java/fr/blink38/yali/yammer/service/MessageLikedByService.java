package fr.blink38.yali.yammer.service;

import org.springframework.stereotype.Service;

import fr.blink38.yali.yammer.entity.MessageLikedBy;
import fr.blink38.yali.yammer.entity.User;
import fr.blink38.yali.yammer.service.yammer.YammerPaginateService;

@Service
public class MessageLikedByService extends YammerPaginateService<MessageLikedBy, User> {

    @Override
    public String getUri() {
        return "/users/liked_message/%s.json";
    }
}