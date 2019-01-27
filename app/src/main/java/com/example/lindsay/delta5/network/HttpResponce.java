package com.example.lindsay.delta5.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-27
 */
public class HttpResponce
{
    String id;
    String project;
    String iteration;
    String created;

    public Prediction[] predictions;


    public HttpResponce(String id, String project, String iteration, String created, Prediction[] predictions) {
        this.id = id;
        this.project = project;
        this.iteration = iteration;
        this.created = created;
        this.predictions = predictions;
    }

    public static class Prediction implements Parcelable
    {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(tagId);
            dest.writeString(tag);
            dest.writeFloat(probability);
        }

        private Prediction(Parcel in) {

            tagId = in.readString();
            tag = in.readString();
            probability = in.readFloat();
        }


        public static final Creator<Prediction> CREATOR = new Creator<Prediction>() {
            @Override
            public Prediction createFromParcel(Parcel in) {
                return new Prediction(in);
            }

            @Override
            public Prediction[] newArray(int size) {
                return new Prediction[size];
            }
        };


        String tagId;
        public String tag;
        public float probability;


        public Prediction(String tagId, String tag, float probability) {
            this.tagId = tagId;
            this.tag = tag;
            this.probability = probability;
        }
    }


}
