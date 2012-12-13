package com.antew.redditinpictures.library.reddit;

public enum SubscribeAction {
    SUBSCRIBE("sub"), UNSUBSCRIBE("unsub");

    String action;

    SubscribeAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}