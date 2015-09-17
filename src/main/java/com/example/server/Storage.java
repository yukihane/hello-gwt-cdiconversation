package com.example.server;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;

@ConversationScoped
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Conversation conversation;

    private int number;

    public synchronized int add(int number) {
        this.number += number;
        return this.number;
    }

    public int getNumber() {
        return number;
    }

    public void begin() {
        conversation.begin();
    }

    public void end() {
        conversation.end();
    }

    public String getCid() {
        return conversation.getId();
    }
}
