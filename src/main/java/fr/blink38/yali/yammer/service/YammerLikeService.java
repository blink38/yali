package fr.blink38.yali.yammer.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import fr.blink38.yali.entity.AccessToken;
import fr.blink38.yali.entity.Community;
import fr.blink38.yali.service.LikeService;
import fr.blink38.yali.yammer.entity.Group;
import fr.blink38.yali.yammer.entity.Message;
import fr.blink38.yali.yammer.entity.MessagesInGroup;
import fr.blink38.yali.yammer.entity.User;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class YammerLikeService implements LikeService {

    @Autowired
    CurrentUserService currentUser;

    @Autowired
    GroupsService groupService;

    @Autowired
    MessagesInGroupService messageService;

    @Autowired
    MessageLikeService likeService;

    @Override
    public int like(List<Community> communities, AccessToken token) {

        User user;

        try {
            user = currentUser.query(Collections.emptyList(), Collections.emptyMap(),
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
                MessagesInGroup messages = messageService.query(List.of(group.getId()), Collections.emptyMap(),
                        token.getToken());

                for (Message message : messages.getMessages()) {

                    log.info(String.format("MESSAGE : %s - %s", message.getId(),
                            StringUtils.substring(StringUtils.split(message.getBody().getPlain(), "\n")[0], 0,
                                    40)));

                    likeService.post(Collections.emptyList(),
                            Map.of("message_id", message.getId()), token.getToken());

                    log.info(String.format("LIKE : %s - %s ", message.getId(), message.getUrl()));
                }

            } catch (WebClientResponseException ex) {

            }

        }
        return 0;
    }
    

    public Collection<Group> getGroupsForUser(User user, AccessToken token) {

        Collection<Group> groups = groupService.queryAll(List.of(user.getId()), Collections.emptyMap(),
                token.getToken());

        groups.stream().forEach(group -> {
            log.info("GROUP : " + group.getId() + " - " + group.getFull_name());
        });

        return groups;
    }

}
