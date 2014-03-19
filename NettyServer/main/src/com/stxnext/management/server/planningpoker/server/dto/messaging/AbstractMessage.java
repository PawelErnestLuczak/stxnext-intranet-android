package com.stxnext.management.server.planningpoker.server.dto.messaging;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public abstract class AbstractMessage {

    protected Type listStringType = new TypeToken<ArrayList<String>>() {
    }.getType();

    public String serialize() {
        prepareToSerialization();
        return GsonProvider.get().toJson(this);
    }

    public static <T> T fromJsonString(String jsonString, Type type) {
        return GsonProvider.get().fromJson(
                jsonString,
                type);
    }
    
    public abstract void prepareToSerialization();
    
}