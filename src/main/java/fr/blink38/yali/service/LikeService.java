package fr.blink38.yali.service;

import java.util.List;

import fr.blink38.yali.entity.AccessToken;
import fr.blink38.yali.entity.Community;

public interface LikeService {
    
    /**
     * Add Like to message
     * @param token access token used to identify user
     * @return like count done
     * @throws Exception 
     */
    int like(List<Community> communities, AccessToken token);
}
