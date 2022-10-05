package fr.blink38.yali.yammer.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RestService<T> {
    
    public Optional<T> query(List<String> params, String accessToken);

    public Collection<T> queryAll(List<String> params, String accessToken);
}
