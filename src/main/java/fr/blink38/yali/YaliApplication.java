package fr.blink38.yali;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import fr.blink38.yali.service.LikeService;
import fr.blink38.yali.yammer.YammerProps;

@SpringBootApplication
@Profile("!test")
public class YaliApplication
    implements CommandLineRunner {

  @Autowired
  LikeService likeService;

  @Autowired
  private YammerProps yammerProps;

  private static Logger LOG = LoggerFactory
      .getLogger(YaliApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(YaliApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    LOG.info("Found " + yammerProps.getAccessTokens().size() + " access token");

    yammerProps.getAccessTokens().stream().forEach(token -> {
      likeService.like(yammerProps.getCommunities(), token);
    });

  }
}