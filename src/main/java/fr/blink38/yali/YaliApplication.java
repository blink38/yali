package fr.blink38.yali;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fr.blink38.yali.yammer.ApplicationProps;
import fr.blink38.yali.yammer.entity.Group;
import fr.blink38.yali.yammer.entity.User;
import fr.blink38.yali.yammer.service.CurrentUserService;
import fr.blink38.yali.yammer.service.GroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YaliApplication
    implements CommandLineRunner {

  @Autowired
  CurrentUserService currentUser;

  @Autowired
  GroupsService groupService;

  @Autowired
  private ApplicationProps applicationProps;

  private static Logger LOG = LoggerFactory
      .getLogger(YaliApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(YaliApplication.class, args).close();
  }

  @Override
  public void run(String... args) {
    LOG.info("EXECUTING : command line runner");

    applicationProps.getAccessTokens().stream().forEach(token -> System.out.println(token.getName()));

    for (int i = 0; i < args.length; ++i) {
      LOG.info("args[{}]: {}", i, args[i]);
    }

    Optional<User> user = currentUser.query(Collections.emptyList(),
        applicationProps.getAccessTokens().get(0).getToken());

    System.out.println("USER : " + user.get().getFull_name());

    // yammer.getCurrentUser();
    groupService.queryAll(List.of("1649005135"), applicationProps.getAccessTokens().get(0).getToken()).stream()
        .forEach(group -> {
          System.out.println("GROUP : " + group.getId() + " - " + group.getFull_name());
        });

  }
}