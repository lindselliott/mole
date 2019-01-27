package com.example.lindsay.delta5.network;

/**
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-27
 */
public class HttpResponce
{
    String Id;
    String Project;
    String Iteration;
    String Created;

    Prediction[] predictions;


    public HttpResponce(String id, String project, String iteration, String created, Prediction[] predictions) {
        Id = id;
        Project = project;
        Iteration = iteration;
        Created = created;
        this.predictions = predictions;
    }

    static class Prediction {

        String TagId;
        String Tag;
        float Probability;


        public Prediction(String tagId, String tag, float probability) {
            TagId = tagId;
            Tag = tag;
            Probability = probability;
        }
    }


}
