package fr.blink38.yali.yammer.service;

import org.springframework.stereotype.Service;

import fr.blink38.yali.yammer.entity.MessagesInGroup;
import fr.blink38.yali.yammer.service.yammer.YammerService;

@Service
public class MessagesInGroupService extends YammerService<MessagesInGroup> {

    @Override
    public String getUri() {
        return "/messages/in_group/%s.json";
    }

}
