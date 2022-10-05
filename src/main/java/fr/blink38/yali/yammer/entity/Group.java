package fr.blink38.yali.yammer.entity;

import lombok.Data;

@Data
public class Group {
    
    String id;
    String full_name;
    String description;
    String privacy;
}