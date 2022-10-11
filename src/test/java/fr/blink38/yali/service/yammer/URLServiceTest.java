package fr.blink38.yali.service.yammer;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import fr.blink38.yali.service.URLService;

@ActiveProfiles("test")
public class URLServiceTest {

    @Test
    void givenUriParameter_andQueryParameters_whenBuildUri_thenSuccess() throws Exception {

        URI uri = URLService.instance().setUrl("http://baseurl/").setPath("/%s/uri.json").setPathParameter(List.of("12345"))
                .addParameter("key1", "value1")
                .addParameter("key2", "value2")
                .build();

        Assertions.assertEquals("http://baseurl/12345/uri.json?key1=value1&key2=value2", uri.toString());
    }

    @Test
    void givenUriParameter_whenBuildUri_thenSuccess() throws Exception {

        URI uri = URLService.instance().setUrl("http://baseurl/").setPath("/%s/uri.json").setPathParameter(List.of("12345"))
                .build();

        Assertions.assertEquals("http://baseurl/12345/uri.json", uri.toString());
    }

    @Test
    void givenUriParameters_whenBuildUri_thenSuccess() throws Exception {

        URI uri = URLService.instance().setUrl("http://baseurl/").setPath("/%s/%s/uri.json").setPathParameter(List.of("12345", "azerty"))
                .build();

        Assertions.assertEquals("http://baseurl/12345/azerty/uri.json", uri.toString());
    }
}
