package com.example.lindsay.delta5.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

  //  public void makeConnection() {
        String url = "https://eastus.api.cognitive.microsoft.com/customvision/v2.0/Prediction/de28272f-1578-426f-b82a-e40f49d8d024/image";


       // Set Prediction-Key Header to : 43efe80ae7e94b6f8a36a7841cd3ac8f
       // Set Content-Type Header to : application/octet-stream
        //Set Body to : <image file>

        public static final MediaType OCTETSTREAM
                = MediaType.get("application/octet-stream");


        OkHttpClient client = new OkHttpClient();

    Request request;

        public String post(File file) throws IOException {


            RequestBody body = RequestBody.create(OCTETSTREAM, file);

            request = new Request.Builder()
                    .url(url)
                    .addHeader("Prediction-Key", "43efe80ae7e94b6f8a36a7841cd3ac8f")
                    .post(body)
                    .build();


            new Task().execute();

            return "";




        }


        class Task extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {

                try (Response response = client.newCall(request).execute()) {

                    Log.d("deltahacks", response.body().string());
                    //return response.body().string();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
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
