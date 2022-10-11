package fr.blink38.yali.yammer.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import fr.blink38.yali.yammer.service.yammer.YammerService;

@Service
public class MessageLikeService extends YammerService<String> {

    private final String URI = "/messages/liked_by/current.json";
    // ?message_id=1943383973257216";

    @Override
    public String getUri(List<String> params) {
        return this.URI;
    }

    @Override
    public MultiValueMap<String, String> getQueryParameters(Map<String,String> params) {

        MultiValueMap<String,String> map =  new LinkedMultiValueMap<String,String>();
        
        params.keySet().stream().forEach(key -> {
            map.add(key, params.get(key));
        });
     
        return map;
    }


}