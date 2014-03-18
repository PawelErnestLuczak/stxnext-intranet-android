package com.stxnext.management.server.planningpoker.server.dto.messaging.out;

public enum NotificationFor {

    UserConnectionState("user_connection_state"),
    NextTicket("next_ticket"),
    UserVote("userVote");

    private String action;

    NotificationFor(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
    
}
