package fr.blink38.yali.yammer.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.blink38.yali.entity.AccessToken;
import fr.blink38.yali.entity.Community;
import fr.blink38.yali.service.LikeService;
import fr.blink38.yali.yammer.entity.Group;
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

    @Override
    public int like(List<Community> communities, AccessToken token) {

        Optional<User> user = currentUser.query(Collections.emptyList(),
                token.getToken());

        if (user.isEmpty()) {
            log.error(String.format("user token %s not found or invalid", token.getName()));
            return 0;
        }

        log.info("USER : " + user.get().getFull_name());

        Collection<Group> groups = this.getGroupsForUser(user.get(), token);

        for (Group group : groups){

            if (! communities.stream().anyMatch(community -> StringUtils.equals(community.getId(), group.getId())) ){
                log.info("SKIP community " + group.getId() + " - " + group.getFull_name());
                continue;
            }

            Optional<MessagesInGroup> messages = messageService.query(List.of(group.getId()), token.getToken());

            if (messages.isPresent()) {

                messages.get().getMessages().stream().forEach(message -> {

                    log.info("MESSAGE : " + message.getId() + " - like ");// + message.getLiked_by().getCount());
                });

            }

        }
        return 0;
    }

    public Collection<Group> getGroupsForUser(User user, AccessToken token) {

        Collection<Group> groups = groupService.queryAll(List.of(user.getId()), token.getToken());

        groups.stream().forEach(group -> {
            log.info("GROUP : " + group.getId() + " - " + group.getFull_name());
        });

        return groups;
    }

}
