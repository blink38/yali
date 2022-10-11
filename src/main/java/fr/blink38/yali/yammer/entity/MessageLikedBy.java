package fr.blink38.yali.yammer.entity;

import java.util.List;

import lombok.Data;

@Data
public class MessageLikedBy {

    List<User> users;

    Boolean more_available;
    Integer total_count;

    public List<User> getUsers(){
        return this.users;
    }

    public MessageLikedBy setUsers(List<User> users){
        this.users = users;
        return this;
    }
}
