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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import fr.blink38.yali.yammer.entity.Group;
import fr.blink38.yali.yammer.service.GroupsService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest(classes = { GroupsService.class })
@ActiveProfiles("test")
public class GroupsServiceTest {

    @Autowired
    GroupsService service;

    public static MockWebServer mockBackEnd;

    URLService uri;

    @Value("classpath:json/groupsService.json")
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
    public void whenRequest_thenGroupsIsSuccess() throws IOException {

        mockBackEnd.enqueue(new MockResponse()
                .setBody(FileUtils.readFileToString(json.getFile(), StandardCharsets.UTF_8))
                .addHeader("Content-Type", "application/json"));

        Collection<Group> groups = service.queryAll(uri.setPathParameter(List.of("1234")), "token");
                        
        Assertions.assertEquals(3, groups.size());
    }

}
