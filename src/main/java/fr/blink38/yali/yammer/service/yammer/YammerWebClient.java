package fr.blink38.yali.yammer.service.yammer;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class YammerWebClient {

    protected WebClient client;

    @PostConstruct
    public void init() {

        this.client = WebClient.builder()
                .baseUrl("https://www.yammer.com/api/v1")
                .build();
    }

    public ResponseSpec query(String uri, HttpMethod method, MultiValueMap<String,String> queryParams, String accessToken) throws WebClientResponseException {
        
        RequestBodyUriSpec uriSpec = client.method(method);

        RequestBodySpec bodySpec = uriSpec.uri(
            uriBuilder -> uriBuilder.path(uri).queryParams(queryParams).build());

        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("");

        headersSpec.header(
                HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();


        return headersSpec.retrieve();
                    // .onStatus(
                    // HttpStatus.UNAUTHORIZED::equals, resp -> {
                    // System.out.println("NON AUTORISE");
                    // System.out.println(resp.bodyToMono(String.class).block()); })

                    // .bodyToMono(classz);
                // return response;
    }

    // public void getCurrentUser(String accessToken) {

    // RequestBodyUriSpec uriSpec = client.method(HttpMethod.GET);

    // RequestBodySpec bodySpec = uriSpec.uri("/users/current.json");

    // RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("");

    // headersSpec.header(
    // HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    // .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
    // .accept(MediaType.APPLICATION_JSON)
    // .acceptCharset(StandardCharsets.UTF_8)
    // .retrieve();

    // try {

    // Mono<User> response = headersSpec.retrieve()
    // // .onStatus(
    // // HttpStatus.UNAUTHORIZED::equals, resp -> {
    // // System.out.println("NON AUTORISE");
    // // System.out.println(resp.bodyToMono(String.class).block()); })

    // .bodyToMono(User.class);

    // System.out.println(response.block().getFull_name());

    // } catch (WebClientResponseException e) {
    // System.err.println(e.getStatusCode().value());
    // }

    // }

}
