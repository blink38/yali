package fr.blink38.yali.yammer.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import fr.blink38.yali.yammer.entity.User;
import fr.blink38.yali.yammer.service.yammer.YammerService;

@Service
public class CurrentUserService extends YammerService<User> {

    private final String URI = "/users/current.json";

    @Override
    public String getUri(List<String> params){
        return this.URI;
    }

    @Override
    public MultiValueMap<String, String> getQueryParameters(Map<String,String> params){
        return new LinkedMultiValueMap<String,String>();
    }
}
