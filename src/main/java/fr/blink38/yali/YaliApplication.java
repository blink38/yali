package fr.blink38.yali;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import fr.blink38.yali.service.LikeService;
import fr.blink38.yali.yammer.YammerProps;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Profile("!test")
@Log4j2
public class YaliApplication
    implements CommandLineRunner {

  @Autowired
  LikeService likeService;

  @Autowired
  private YammerProps yammerProps;

  public static void main(String[] args) {
    SpringApplication.run(YaliApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    yammerProps.getAccessTokens().stream().forEach(token -> {

      int count = likeService.like(yammerProps.getCommunities(), token);
      log.info(String.format("Like count = %d", count));
    });

  }
}