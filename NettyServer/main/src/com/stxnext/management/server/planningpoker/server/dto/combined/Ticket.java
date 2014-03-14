package com.stxnext.management.server.planningpoker.server.dto.combined;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.stxnext.management.server.planningpoker.server.dto.messaging.AbstractMessage;

@DatabaseTable(tableName = Ticket.ENTITY_NAME)
public class Ticket extends AbstractMessage {

    public static final String ENTITY_NAME = "poker_ticket"; 
    
    public static final String FIELD_ID = "id";
    public static final String FIELD_DISPLAY_VALUE = "display_value";
    public static final String JSON_FIELD_VOTES = "votes";
    
    public Ticket(){};
    
    @Expose
    @SerializedName(FIELD_ID)
    @DatabaseField(generatedId = true, columnName = FIELD_ID)
    private long id;
    
    @Expose
    @SerializedName(JSON_FIELD_VOTES)
    @ForeignCollectionField
    private ForeignCollection<Vote> votes;
    
    @Expose
    @SerializedName(FIELD_DISPLAY_VALUE)
    @DatabaseField(columnName = FIELD_DISPLAY_VALUE)
    private String displayValue;

    public long getId() {
        return id;
    }

    public ForeignCollection<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ForeignCollection<Vote> votes) {
        this.votes = votes;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
    
}