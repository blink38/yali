package fr.blink38.yali.service;

import java.util.Collection;

public interface RestService<T> {
    
    public Collection<T> queryAll(URLService uri, String accessToken);

    public T query(URLService uri, String accessToken);
    public T post(URLService uri, String accessToken);

}
