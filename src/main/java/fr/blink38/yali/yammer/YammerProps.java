package fr.blink38.yali.yammer;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.blink38.yali.entity.AccessToken;
import fr.blink38.yali.entity.Community;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Component
@ConfigurationProperties(prefix = "yammer")
@Data
@Log4j2
public class YammerProps {

    private List<AccessToken> accessTokens;

    private List<Community> communities;


    @PostConstruct
    void check(){

        Assert.notNull(accessTokens,"token list not found, please check yammer.access-token your application.yml");

        log.info("Found " + accessTokens.size() + " access token");
    }

}
