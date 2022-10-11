package fr.blink38.yali.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import fr.blink38.yali.yammer.entity.User;
import fr.blink38.yali.yammer.service.CurrentUserService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest(classes = { CurrentUserService.class })
@ActiveProfiles("test")
public class CurrentUserServiceTest {

    @Autowired
    CurrentUserService service;

    public static MockWebServer mockBackEnd;

    URLService uri;

    @Value("classpath:json/currentUserService.json")
    Resource json;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());

        uri = URLService.instance().setUrl(baseUrl);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void whenRequest_thenUserIsSuccess() throws IOException {

        mockBackEnd.enqueue(new MockResponse()
                .setBody(FileUtils.readFileToString(json.getFile(), StandardCharsets.UTF_8))
                .addHeader("Content-Type", "application/json"));

        User user = service.query(uri, "token");
                        
        Assertions.assertEquals("DOE John", user.getFull_name());
        Assertions.assertEquals("1649005135", user.getId());
        Assertions.assertEquals("user", user.getType());
        Assertions.assertEquals("staff", user.getJob_title());
        
    }

}
