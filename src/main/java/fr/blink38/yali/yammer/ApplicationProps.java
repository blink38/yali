package fr.blink38.yali.yammer;

import java.util.List;

import fr.blink38.yali.yammer.entity.AccessToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProps {
    
    private List<AccessToken> accessTokens;

}
