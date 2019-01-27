package com.example.lindsay.delta5.network;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-27
 */
public class HttpConnection
{

    public static String AI_RESPONCE_ACTION = "com.example.linday.delta5.AI_RESPONCE_ACTION";
    public static String PREDICTIONS_EXTRA = "PREDICTIONS_EXTRA";
    public static String SUCCESS_EXTRA = "SUCCESS_EXTRA";


    //  public void makeConnection() {
        String url = "https://eastus.api.cognitive.microsoft.com/customvision/v2.0/Prediction/de28272f-1578-426f-b82a-e40f49d8d024/image";


       // Set Prediction-Key Header to : 43efe80ae7e94b6f8a36a7841cd3ac8f
       // Set Content-Type Header to : application/octet-stream
        //Set Body to : <image file>

        public static final MediaType OCTETSTREAM
                = MediaType.get("application/octet-stream");


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30*1000, TimeUnit.MILLISECONDS)
                .readTimeout(15*1000, TimeUnit.MILLISECONDS)
                .writeTimeout(10*1000, TimeUnit.MILLISECONDS)
                .build();


        public void post(File file, Context context)  {

            RequestBody body = RequestBody.create(OCTETSTREAM, file);

            Request request;

            request = new Request.Builder()
                    .url(url)
                    .addHeader("Prediction-Key", "43efe80ae7e94b6f8a36a7841cd3ac8f")
                    .post(body)
                    .build();

            new Task(context).execute(request);
        }


        class Task extends AsyncTask<Request, Void, HttpResponce>{

            Context context;

            public Task(Context context) {

                this.context = context;

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d("deltahacks", "no permissions");
                }
            }

            @Override
            protected HttpResponce doInBackground(Request... request) {

                try (Response response = client.newCall(request[0]).execute()) {

                    String respStr = response.body().string();
                    Log.d("deltahacks", respStr);


                    if (response.code() != 200) {
                        return null;
                    }

                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(HttpResponce.class, new ResponceDeserializer())
                            .registerTypeAdapter(HttpResponce.Prediction.class, new PredictionDeserializer())
                            .create();


                    HttpResponce data = gson.fromJson(respStr, HttpResponce.class);

                    for (int i = 0; i < data.predictions.length; i++) {
                        Log.d("deltahacks", data.predictions[i].tag + " : " + data.predictions[i].probability);
                    }

                    return data;
                }
                catch (IOException e) {
                    Log.e("deltahacks", "err", e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(HttpResponce httpResponce) {
                super.onPostExecute(httpResponce);

                Intent intent = new Intent(AI_RESPONCE_ACTION);

                if (httpResponce == null) {
                    Log.d("deltahacks", "error with the http response");

                    intent.putExtra(SUCCESS_EXTRA, false);
                } else {
                    ArrayList<HttpResponce.Prediction> arrayList = new ArrayList<HttpResponce.Prediction>(Arrays.asList(httpResponce.predictions));

                    intent.putParcelableArrayListExtra(PREDICTIONS_EXTRA, arrayList);
                    intent.putExtra(SUCCESS_EXTRA, true);
                }

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        }

    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while((nbread=is.read(data))>-1){
                fos.write(data,0,nbread);
            }
        }
        catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
        finally{
            if (fos!=null){
                fos.close();
            }
        }
    }
}
