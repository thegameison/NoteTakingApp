package com.example.notetakingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class HttpAsyncTask extends AsyncTask<String, Integer, HashSet<String>> {
    private static final String subscriptionKey = "0573248bd8834a59a5d2c2985f11490f"; // CENSOR OUT THIS KEY FOR PRIVACY
    private static final String endpoint = ("https://a1notetaker.cognitiveservices.azure.com/");
    private static final String path = "vision/v2.0/read/core/asyncBatchAnalyze";
    HashSet<String> words;

    @Override
    protected HashSet<String> doInBackground(String... strings) {
        words = new HashSet<>();
        try{
            long time1 = System.currentTimeMillis();
            URL url = new URL(endpoint+path);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
            connection.setDoInput(true);
            connection.connect();
            //Log.e("AsyncTask", "We have set the http connection");
            OutputStream out = connection.getOutputStream(); //may need to change to OutputStream
            Bitmap img = BitmapFactory.decodeFile(strings[0]);
            Log.e("AsyncTask", "IMG path: " + strings[0]);
            img.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.close();

            //Log.e("CreateNote", "\nText submitted.\n" + "Waiting 10 seconds to retrieve the recognized text.\n");
            Thread.sleep(5000);

            int status = connection.getResponseCode();
            String headerNew = "";
            if (status == HttpsURLConnection.HTTP_ACCEPTED){
                //Log.w("CreateNote", "Http Status: " + status);
                headerNew = connection.getHeaderField("Operation-Location");
                Log.w("CreateNote", "Operation-Location: " + headerNew);
                        /*keyNew = connection.getHeaderField("apim-request-id");
                        Log.w("CreateNote", "apim-request-id: " + keyNew);*/
            }

            connection.disconnect();

            URL getLocation = new URL(headerNew);
            HttpsURLConnection getResponseConn = (HttpsURLConnection) getLocation.openConnection();

            getResponseConn.setRequestMethod("GET");
            getResponseConn.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

            getResponseConn.connect();

            //Log.w("CreateNote", "GET Connection has been set");
            Thread.sleep(5000);
            //Log.w("CreateNote", "GET Status: " + getResponseConn.getResponseCode());

            BufferedReader in = new BufferedReader(new InputStreamReader(getResponseConn.getInputStream())); //Changed to GET connection, fixed the error
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null){
                response.append(line);
            }
            in.close();
            // Log.w("CreateNote", "JSON Result: " + response.toString());
            JSONObject result = new JSONObject(response.toString());
            //Log.w("First parse", "" + result);
            //Log.w("JSON Parse result", result.toString());
            JSONArray recog = result.getJSONArray("recognitionResults");
            JSONObject recogOb = recog.getJSONObject(0);
            JSONArray lines = recogOb.getJSONArray("lines");
            for (int i = 0; i < lines.length(); i++){
                JSONObject word = lines.getJSONObject(i);
                // Log.w("Update Words: ", word.getString("text"));
                words.add(word.getString("text"));
            }
            //Log.w("JSON Parse recog", recog.toString(2));
            Log.w("AsyncTask", "Task Time: " + ((System.currentTimeMillis()-time1)/1000.0));

        } catch (Exception e){
            e.printStackTrace();
        }
        return words;
    }
    protected void onPostExecute(HashSet<String> result) {
        Iterator<String> itr = result.iterator();
        while (itr.hasNext()){
            Log.w("AsyncTask Result", itr.next());
        }
    }
}
