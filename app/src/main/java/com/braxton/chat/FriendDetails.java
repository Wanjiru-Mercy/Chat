package com.braxton.chat;

/**
 * Created by Braxton on 10/14/2017.
 */

public class FriendDetails {
    public String friend;
    public String lastText = "";

    public FriendDetails(String friend, String lastText) {
        this.friend = friend;
        this.lastText = lastText;
    }

    public String getFriend() {
        return friend;
    }

    public String getLastText() {
        return lastText;
    }
}
