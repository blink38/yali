package fr.blink38.yali.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RestService<T> {
    
    public T query(List<String> UriParameters, Map<String,String> queryParameters, String accessToken);
    public T post(List<String> UriParameters, Map<String,String> queryParameters, String accessToken);

    public Collection<T> queryAll(List<String> UriParameters, Map<String,String> queryParameters, String accessToken);
}
