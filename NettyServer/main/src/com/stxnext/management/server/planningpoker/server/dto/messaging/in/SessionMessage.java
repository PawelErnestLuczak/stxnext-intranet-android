package com.stxnext.management.server.planningpoker.server.dto.messaging.in;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stxnext.management.server.planningpoker.server.dto.combined.Player;
import com.stxnext.management.server.planningpoker.server.dto.combined.Session;
import com.stxnext.management.server.planningpoker.server.dto.messaging.AbstractMessage;

public class SessionMessage  extends AbstractMessage {

    @Expose
    @SerializedName("session_id")
    private Long sessionId;
    @Expose
    @SerializedName("player_id")
    private Long playerId;
    @Expose
    @SerializedName("session_subject")
    private Object sessionSubject;

    public SessionMessage(Player player, Session session, Object entity){
        this.playerId = player.getId();
        this.sessionId = session.getId();
        this.sessionSubject = entity;
    }
    
    public SessionMessage(Long playerId, Long sessionId, Object entity){
        this.playerId = playerId;
        this.sessionId = sessionId;
        this.sessionSubject = entity;
    }
    
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Object getSessionSubject() {
        return sessionSubject;
    }

    public void setSessionSubject(Object sessionSubject) {
        this.sessionSubject = sessionSubject;
    }

    @Override
    public void prepareToSerialization() {
        if(sessionSubject != null && sessionSubject instanceof AbstractMessage){
            ((AbstractMessage) sessionSubject).prepareToSerialization();
        }
    }

}
