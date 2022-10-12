package fr.blink38.yali.yammer.service.yammer;

import java.net.URI;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import fr.blink38.yali.service.RestService;
import fr.blink38.yali.service.URLService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public abstract class YammerService<T> extends YammerWebClient implements RestService<T> {

    Class<T> typeParameterClass;
    
    @SuppressWarnings("unchecked")
    @PostConstruct 
    void initTypeParameterClass(){
        this.typeParameterClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), YammerService.class);
    }


    @Override
    public Collection<T> queryAll(URLService uri, String accessToken) throws WebClientResponseException {

        uri.setPath(this.getUri());

        return this.queryAll(typeParameterClass, uri.build(), accessToken);
    }

    private Collection<T> queryAll(Class<T> entity, URI uri, String accessToken) throws WebClientResponseException  {

        try {
            ResponseSpec response = this.query(uri, HttpMethod.GET, accessToken);
            return response.bodyToFlux(entity).collectList().block();

        } catch (WebClientResponseException ex) {
            log.error(String.format("HTTP code=%s / %s", ex.getStatusCode().value(), uri.toString()));
            throw ex;
        }
    }

    @Override
    public T query(URLService uri, String accessToken) throws WebClientResponseException {

        uri.setPath(this.getUri());

        return this.query(typeParameterClass, HttpMethod.GET, uri.build(), accessToken);
    }

    @Override
    public T post(URLService uri, String accessToken) throws WebClientResponseException {

        uri.setPath(this.getUri());

        return this.query(typeParameterClass, HttpMethod.POST, uri.build(), accessToken);
    }

    private T query(Class<T> entity, HttpMethod method, URI uri, String accessToken) throws WebClientResponseException {

        try {
            ResponseSpec response = this.query(uri, method, accessToken);
            return response.bodyToMono(entity).block();

        } catch (WebClientResponseException ex) {
            log.error(String.format("HTTP code=%s / %s", ex.getStatusCode().value(), uri.toString()));
            throw ex;
        }
    }

}
