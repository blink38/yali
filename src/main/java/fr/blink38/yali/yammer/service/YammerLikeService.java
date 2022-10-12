package fr.blink38.yali.yammer.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import fr.blink38.yali.entity.AccessToken;
import fr.blink38.yali.entity.Community;
import fr.blink38.yali.service.LikeService;
import fr.blink38.yali.service.URLService;
import fr.blink38.yali.yammer.entity.Group;
import fr.blink38.yali.yammer.entity.Message;
import fr.blink38.yali.yammer.entity.MessagesInGroup;
import fr.blink38.yali.yammer.entity.User;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class YammerLikeService implements LikeService {

    @Value("${yammer.base-url:}")
    String baseUrl;

    @Autowired
    CurrentUserService currentUser;

    @Autowired
    GroupsService groupService;

    @Autowired
    MessagesInGroupService messageService;

    @Autowired
    MessageLikeService likeService;

    @Autowired
    MessageLikedByService likedByService;

    @Override
    public int like(List<Community> communities, AccessToken token) {

        User user;
        int likeCount = 0;

        try {
            user = currentUser.query(urlService(),
                    token.getToken());
            log.info("USER found : " + user.getFull_name());

        } catch (WebClientResponseException ex) {
            log.error(String.format("User token %s not found or invalid", token.getName()));
            return 0;
        }

        Collection<Group> groups = this.getGroupsForUser(user, token);

        for (Group group : groups) {

            if (!communities.stream().anyMatch(community -> StringUtils.equals(community.getId(), group.getId()))) {
                log.info("SKIP community " + group.getId() + " - " + group.getFull_name());
                continue;
            }

            try {
                MessagesInGroup messages = messageService.query(urlService()
                        .setPathParameter(List.of(group.getId()))
                        .addParameter("threaded", "true"),
                        token.getToken());

                for (Message message : messages.getMessages()) {

                    Collection<User> users = likedByService.page("users", urlService()
                            .setPathParameter(List.of(message.getId())),
                            token.getToken());

                    if (users.stream().anyMatch(usr -> StringUtils.equals(usr.getId(), user.getId()))) {
                        log.info(String.format("message %s already liked. skip", message.getId()));
                        continue;
                    }

                    try {
                        log.info(String.format("MESSAGE : %s - %s", message.getId(),
                                StringUtils.substring(
                                        StringUtils.split(StringUtils.defaultIfBlank(message.getBody().getPlain(), " "),
                                                "\n")[0],
                                        0,
                                        60)));
                    } catch (Exception e) {
                        log.error(e);
                    }

                    likeService.post(
                            URLService.instance().setUrl(this.baseUrl).addParameter("message_id", message.getId()),
                            token.getToken());

                    likeCount++;
                    log.info(String.format("LIKE : %s - %s ", message.getId(), message.getWeb_url()));
                }

            } catch (WebClientResponseException ex) {
                log.error(ex);
            }

        }
        return likeCount;
    }

    public Collection<Group> getGroupsForUser(User user, AccessToken token) {

        Collection<Group> groups = groupService.queryAll(urlService().setPathParameter(List.of(user.getId())),
                token.getToken());

        groups.stream().forEach(group -> {
            log.info("GROUP : " + group.getId() + " - " + group.getFull_name());
        });

        return groups;
    }

    private URLService urlService() {
        return URLService.instance().setUrl(this.baseUrl);
    }

}
