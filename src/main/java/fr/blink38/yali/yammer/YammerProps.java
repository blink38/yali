package fr.blink38.yali.yammer;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import fr.blink38.yali.entity.AccessToken;
import fr.blink38.yali.entity.Community;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "yammer")
@Data
public class YammerProps {

    private List<AccessToken> accessTokens;

    private List<Community> communities;

}
