package fr.blink38.yali.yammer.service.yammer;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public abstract class YammerWebClient {

    protected WebClient client;

    public abstract String getUri();

    @PostConstruct
    public void init() {

        // avoid DataBufferLimitException : https://www.amitph.com/spring-webclient-large-file-download/
        this.client = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(4 * 1024 * 1024))
                        .build())
                .build();
    }

    protected ResponseSpec query(URI uri, HttpMethod method, 
    // MultiValueMap<String, String> queryParams,
            String accessToken) throws WebClientResponseException {

        RequestBodyUriSpec uriSpec = client.method(method);

        RequestBodySpec bodySpec = uriSpec.uri(uri);
                // uriBuilder -> uriBuilder.path(uri).queryParams(queryParams).build());

        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("");

        headersSpec.header(
                HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();

        return headersSpec.retrieve();
    }


}
