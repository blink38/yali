package fr.blink38.yali.yammer.service;

import java.util.List;

import fr.blink38.yali.yammer.entity.Group;
import fr.blink38.yali.yammer.service.yammer.YammerService;
import org.springframework.stereotype.Service;

@Service
public class GroupsService extends YammerService<Group> {

    private final String URI = "/groups/for_user/%s.json";

    @Override
    public String getUri(List<String> params) {
        return String.format(this.URI, params.get(0));
    }

}
