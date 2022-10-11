package fr.blink38.yali.service;

import java.util.Collection;

public interface PaginateRestService<T, V> {
    
    public Collection<V> page(String collectionName, URLService uri, String accessToken);


}
