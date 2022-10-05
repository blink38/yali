package fr.blink38.yali.yammer.entity;

import java.util.List;

import lombok.Data;

public class MessagesInGroup {

    
    List<Message> messages;

    public List<Message> getMessages(){
        return this.messages;
    }

    public MessagesInGroup setMessages(List<Message> messages){
        this.messages = messages;
        return this;
    }
}
