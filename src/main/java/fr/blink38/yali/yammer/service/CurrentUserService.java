package fr.blink38.yali.yammer.service;

import org.springframework.stereotype.Service;

import fr.blink38.yali.yammer.entity.User;
import fr.blink38.yali.yammer.service.yammer.YammerService;

@Service
public class CurrentUserService extends YammerService<User> {

    @Override
    public String getUri() {
        return "/users/current.json";
    }
}
