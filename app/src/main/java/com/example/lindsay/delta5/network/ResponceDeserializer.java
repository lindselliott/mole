package com.example.lindsay.delta5.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-27
 */
public class ResponceDeserializer implements JsonDeserializer<HttpResponce>
{

    @Override
    public HttpResponce deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        JsonObject postObject = json.getAsJsonObject();

        String id = postObject.get("id").getAsString();
        String project = postObject.get("project").getAsString();
        String iteration  = postObject.get("iteration").getAsString();
        String created = postObject.get("created").getAsString();


        JsonArray list = postObject.get("predictions").getAsJsonArray();

        HttpResponce.Prediction[] predictions = context.deserialize(list, HttpResponce.Prediction[].class);


        return new HttpResponce(id, project, iteration, created, predictions);
    }
}

