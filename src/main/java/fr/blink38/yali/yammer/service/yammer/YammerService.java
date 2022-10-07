package fr.blink38.yali.yammer.service.yammer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import fr.blink38.yali.service.RestService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public abstract class YammerService<T> implements RestService<T> {

    @Autowired
    protected YammerWebClient client;

    Class<T> typeParameterClass;

    public abstract String getUri(List<String> params);
    public abstract MultiValueMap<String,String> getQueryParameters(Map<String,String> params);

    @SuppressWarnings("unchecked")
    @PostConstruct 
    void init(){
        this.typeParameterClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), YammerService.class);
        
    }

    @Override
    public Collection<T> queryAll(List<String> UriParameters, Map<String,String> queryParameters, String accessToken) throws WebClientResponseException {

        return this.queryAll(typeParameterClass, UriParameters, queryParameters, accessToken);
    }


    private Collection<T> queryAll(Class<T> entity, List<String> UriParameters, Map<String,String> queryParameters, String accessToken) throws WebClientResponseException  {

        try {
            ResponseSpec response = client.query(this.getUri(UriParameters), HttpMethod.GET, this.getQueryParameters(queryParameters), accessToken);
            return response.bodyToFlux(entity).collectList().block();

        } catch (WebClientResponseException ex) {
            log.error(String.format("HTTP code=%s / %s", ex.getStatusCode().value(), this.getUri(UriParameters)));
            throw ex;
            // return Collections.emptyList();
        }
    }


    @Override
    public T query(List<String> UriParameters, Map<String,String> queryParameters, String accessToken) throws WebClientResponseException {

        return this.query(typeParameterClass, HttpMethod.GET, UriParameters, queryParameters, accessToken);
    }

    @Override
    public T post(List<String> UriParameters, Map<String,String> queryParameters, String accessToken) throws WebClientResponseException {

        return this.query(typeParameterClass, HttpMethod.POST, UriParameters, queryParameters, accessToken);
    }


    private T query(Class<T> entity, HttpMethod method, List<String> UriParameters, Map<String,String> queryParameters, String accessToken) throws WebClientResponseException {

        try {
            ResponseSpec response = client.query(this.getUri(UriParameters), method, this.getQueryParameters(queryParameters), accessToken);
            return response.bodyToMono(entity).block();

        } catch (WebClientResponseException ex) {
            log.error(String.format("HTTP code=%s / %s", ex.getStatusCode().value(), this.getUri(UriParameters)));
            throw ex;
            // return Optional.empty();
        }
    }

}
