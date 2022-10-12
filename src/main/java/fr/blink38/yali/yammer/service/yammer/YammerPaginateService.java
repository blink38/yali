package fr.blink38.yali.yammer.service.yammer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import fr.blink38.yali.service.PaginateRestService;
import fr.blink38.yali.service.URLService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public abstract class YammerPaginateService<T, V> extends YammerWebClient implements PaginateRestService<T, V> {

    Class<T> parentTypeParameterClass;
    Class<V> childrenTypeParameterClass;

    @SuppressWarnings("unchecked")
    @PostConstruct
    void initTypeParameterClass() {

        Class<?> clazz[] = GenericTypeResolver.resolveTypeArguments(getClass(), YammerPaginateService.class);
        // this.parentTypeParameterClass = clazz[1];
        // //enericTypeResolver.resolveTypeArguments(getClass(),
        // YammerPaginateService.class)[0];

        if (clazz != null) {
            parentTypeParameterClass = (Class<T>) clazz[0];
            childrenTypeParameterClass = (Class<V>) clazz[1];
        }
    }

    @Override
    public Collection<V> page(String collectionName, URLService uri,
            String accessToken) throws WebClientResponseException {

        uri.setPath(this.getUri());

        return this.page(parentTypeParameterClass, collectionName, HttpMethod.GET, uri,
                accessToken);
    }

    @SuppressWarnings("unchecked")
    private Collection<V> page(Class<T> entity, String collectionName, HttpMethod method, URLService uri,
            String accessToken) throws WebClientResponseException {

        Collection<V> children = new ArrayList<>();

        int page = 0;
        boolean ended = false;

        while (!ended) {

            try {
                uri.addParameter("page", String.valueOf(page));

                ResponseSpec response = this.query(uri.build(), method, accessToken);
                T result = response.bodyToMono(entity).block();

                try {
                    Method function = result.getClass().getMethod("get" + StringUtils.capitalize(collectionName));

                    Object objects = function.invoke(result);

                    ((List<V>) objects).stream().forEach(item -> children.add(item));

                } catch (Exception e) {
                    log.error(e);
                }

                ended = !this.moreResults(result);
                if (!ended) {
                    page++;
                }

            } catch (WebClientResponseException ex) {
                log.error(String.format("HTTP code=%s / %s", ex.getStatusCode().value(), uri.build().toString()));
                throw ex;
                // return Optional.empty();
            }
        }

        return children;
    }

    /**
     * Is there more results available ?
     * look at more_available value in json response
     * 
     * @param entity
     * @return
     */
    private boolean moreResults(T entity) {

        try {
            Method function = entity.getClass().getMethod("getMore_available");

            Object object = function.invoke(entity);

            if (object instanceof Boolean bool) {
                return bool;
            }

        } catch (Exception e) {
            log.error(e);
            return false;
        }

        return false;
    }
}
