package fr.blink38.yali.yammer.service;

import org.springframework.stereotype.Service;

import fr.blink38.yali.yammer.entity.Group;
import fr.blink38.yali.yammer.service.yammer.YammerService;

@Service
public class GroupsService extends YammerService<Group> {

    @Override
    public String getUri() {
        return "/groups/for_user/%s.json";
    }

}
