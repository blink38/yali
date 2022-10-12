package fr.blink38.yali.yammer.entity;

import lombok.Data;

@Data
public class Message {
    

    String id;
    String sender_id;

    Body body;

    // Like liked_by;

    String url;
    String web_url;
    
    @Data
    public class Body {
        String plain;
    }

    // @Data
    // public class Like {
    //     int count;

    //     LikeUser names;

    //     @Data
    //     public class LikeUser {
    //         String full_name;
    //         String user_id;
    //     }
    // }
}
