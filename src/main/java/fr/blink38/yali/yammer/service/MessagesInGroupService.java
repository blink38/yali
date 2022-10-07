package fr.blink38.yali.yammer.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import fr.blink38.yali.yammer.entity.MessagesInGroup;
import fr.blink38.yali.yammer.service.yammer.YammerService;

@Service
public class MessagesInGroupService extends YammerService<MessagesInGroup> {

    private final String URI = "/messages/in_group/%s.json";

    @Override
    public String getUri(List<String> params) {
        return String.format(this.URI, params.get(0));
    }

    @Override
    public MultiValueMap<String, String> getQueryParameters(Map<String,String> params) {
        return new LinkedMultiValueMap<String,String>(1){{
            add("threaded", "true");
        }};
    }


}
