package fr.blink38.yali.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

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

import fr.blink38.yali.yammer.entity.MessagesInGroup;
import fr.blink38.yali.yammer.entity.User;
import fr.blink38.yali.yammer.service.MessagesInGroupService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MessagesInGroupService.class })
@ActiveProfiles("test")
public class MessagesInGroupServiceTest {

    @Autowired
    MessagesInGroupService service;

    public static MockWebServer mockBackEnd;

    @Value("classpath:json/messageInGroupService.json")
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
        service.setBaseUrl(baseUrl);

        uri = URLService.instance().setUrl(baseUrl);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    URLService uri;

    @Test
    public void whenRequest_thenListIsFullfilled() throws IOException {

        mockBackEnd.enqueue(new MockResponse()
                .setBody(FileUtils.readFileToString(json.getFile(), StandardCharsets.UTF_8))
                .addHeader("Content-Type", "application/json"));

        uri.setPathParameter(List.of("1234"));

        MessagesInGroup messages = service.query(uri, "token");
                        
        Assertions.assertEquals(2, messages.getMessages().size());
    }

}
