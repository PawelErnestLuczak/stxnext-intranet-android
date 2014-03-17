package com.stxnext.management.server.planningpoker.server.dto.combined;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.stxnext.management.server.planningpoker.server.dto.messaging.AbstractMessage;

@DatabaseTable(tableName = Session.ENTITY_NAME)
public class Session  extends AbstractMessage{
    
    public static final String ENTITY_NAME = "poker_session"; 
    
    public static final String FIELD_ID = "id"; 
    public static final String FIELD_START_TIME = "start_time";
    public static final String FIELD_END_TIME = "end_time";
    public static final String FIELD_EXPIRED = "expired";
    public static final String FIELD_OWNER_ID = "owner";
    public static final String FIELD_DECK_ID = "deck_id";
    
    public static final String JSON_FIELD_TICKETS = "tickets";
    public static final String JSON_FIELD_PLAYERS = "players";

    public Session(){}

    @Expose
    @SerializedName(FIELD_ID)
    @DatabaseField(generatedId = true, columnName = FIELD_ID)
    private long id;
    
    @Expose
    @SerializedName(FIELD_START_TIME)
    @DatabaseField(columnName = FIELD_START_TIME)
    private long startTime;
    
    @Expose
    @SerializedName(FIELD_DECK_ID)
    @DatabaseField(columnName = FIELD_DECK_ID)
    private Number deckId;
    
    @Expose
    @SerializedName(FIELD_END_TIME)
    @DatabaseField(columnName = FIELD_END_TIME)
    private long endTime;
    
    @Expose
    @SerializedName(FIELD_EXPIRED)
    @DatabaseField(columnName = FIELD_EXPIRED)
    private boolean expired;
    
    @Expose
    @SerializedName(FIELD_OWNER_ID)
    @DatabaseField(foreign = true, foreignAutoRefresh = true,columnName = FIELD_OWNER_ID)
    private Player owner;
    
    @Expose
    @SerializedName(JSON_FIELD_TICKETS)
    @ForeignCollectionField
    private ForeignCollection<Ticket> tickets;
    
    @Expose
    @SerializedName(JSON_FIELD_PLAYERS)
    //@ForeignCollectionField
    private List<Player> players;

    public long getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    

//    public ForeignCollection<Player> getPlayers() {
//        return players;
//    }
//
//    public void setPlayers(ForeignCollection<Player> players) {
//        this.players = players;
//    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public ForeignCollection<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ForeignCollection<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Number getDeckId() {
        return deckId;
    }

    public void setDeckId(Number deckId) {
        this.deckId = deckId;
    }
    
}
