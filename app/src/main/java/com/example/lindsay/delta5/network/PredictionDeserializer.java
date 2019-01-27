package com.example.lindsay.delta5.network;

import android.util.Log;

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
public class PredictionDeserializer implements JsonDeserializer<HttpResponce.Prediction>
{

    @Override
    public HttpResponce.Prediction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        JsonObject postObject = json.getAsJsonObject();

        Log.d("deltahacks", postObject.toString());

        String tagId = postObject.get("tagId").getAsString();
        String tag = postObject.get("tagName").getAsString();
        float prob  = postObject.get("probability").getAsFloat();

        return new HttpResponce.Prediction(tagId, tag, prob);
    }
}

