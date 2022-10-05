package fr.blink38.yali.yammer.service;

import java.util.List;

import fr.blink38.yali.yammer.entity.User;
import fr.blink38.yali.yammer.service.yammer.YammerService;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService extends YammerService<User> {

    private final String URI = "/users/current.json";

    @Override
    public String getUri(List<String> params){
        return this.URI;
    }
}
