package fr.blink38.yali.service;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.RegExUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class URLService {

    @Setter
    String url;

    @Setter
    String path;

    List<String> pathParams;

    MultiValueMap<String, String> params;

    public static URLService instance() {
        return new URLService();
    }

    public URLService() {
        params = new LinkedMultiValueMap<>();
    }

    public URLService addParameter(String key, String value) {
        params.add(key, value);
        return this;
    }

    public String buildPath() {

        String str = this.path;

        if (this.pathParams != null) {
            for (String param : this.pathParams) {
                str = RegExUtils.replaceFirst(str, "%s", param);
            }
        }
        return str;
    }

    public URLService setPathParameter(List<String> params) {
        this.pathParams = params;
        return this;
    }

    public URI build() {
        return UriComponentsBuilder.fromUriString(this.url).path(this.buildPath()).queryParams(this.params).build()
                .toUri();
    }
}
