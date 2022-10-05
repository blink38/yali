package fr.blink38.yali.yammer.service.yammer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import fr.blink38.yali.yammer.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public abstract class YammerService<T> implements RestService<T> {

    @Autowired
    protected YammerWebClient client;

    Class<T> typeParameterClass;

    public abstract String getUri(List<String> params);


    @SuppressWarnings("unchecked")
    @PostConstruct 
    void init(){
        this.typeParameterClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), YammerService.class);
        
    }

 

    @Override
    public Collection<T> queryAll(List<String> params, String accessToken) {

        return this.queryAll(typeParameterClass, params, accessToken);
    }


    private Collection<T> queryAll(Class<T> entity, List<String> params, String accessToken) {

        try {
            ResponseSpec response = client.query(this.getUri(params), HttpMethod.GET, accessToken);
            return response.bodyToFlux(entity).collectList().block();

        } catch (WebClientResponseException ex) {
            System.err.println(ex.getStatusCode().value());
            return Collections.emptyList();
        }
    }


    @Override
    public Optional<T> query(List<String> params, String accessToken) {

        return this.query(typeParameterClass, params, accessToken);
    }


    private Optional<T> query(Class<T> entity, List<String> params, String accessToken) {

        try {
            ResponseSpec response = client.query(this.getUri(params), HttpMethod.GET, accessToken);
            return Optional.of(
                    response.bodyToMono(entity).block());

        } catch (WebClientResponseException ex) {
            System.err.println(ex.getStatusCode().value());
            return Optional.empty();
        }
    }

}
