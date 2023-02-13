package com.chat.app.handller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ChatMessage implements Serializable{
    private String text;
    private String nameReserved;
    private Set<String> setOnline =new HashSet<String>();
    private Action action;
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNameReserved() {
        return nameReserved;
    }

    public void setNameReserved(String nameReserved) {
        this.nameReserved = nameReserved;
    }

    public Set<String> getSetOnline() {
        return setOnline;
    }

    public void setSetOnline(Set<String> setOnline) {
        this.setOnline = setOnline;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    
    public enum Action {
        CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USER_ONLINE
    }
}
