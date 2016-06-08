package org.simonsays.strimroulette.utils.http;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by szymonkaz on 27/05/2016.
 */

public class HttpUtils {
    private static final String DEBUG_TAG = HttpUtils.class.getSimpleName();
    private static final String CHARSET = "UTF-8";
    private static final String BASE_STREAM_URL = "https://api.twitch.tv/kraken/streams";
    private JSONObject responseObject;


    public String getStream() {
        JSONObject topStreamObj = getTopStream();
        String streamName = getStreamName(topStreamObj);
        Log.d(DEBUG_TAG, "STREAM NAME: " + streamName);
        return getStreamUrl(streamName);
    }

    public String getRandomStream() {
        JSONObject randomStreamObject = getSpecificStream(getRandomNumberWithMax(10000));
        String streamName = getStreamName(randomStreamObject);
        Log.d(DEBUG_TAG, "STREAM NAME: " + streamName);
        return getStreamUrl(streamName);
    }

    public JSONObject getTopStream() {
        InputStream is = null;
        String urlString = "https://api.twitch.tv/kraken/streams";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            responseObject = new JSONObject(responseStrBuilder.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(DEBUG_TAG, "The response is: " + responseObject.toString());
        return responseObject;
    }

    public String getStreamName(JSONObject streamObj) {
        JSONArray jsonArray;
        JSONObject firstStream;
        JSONObject channel;
        String streamTitle = "";
        try {
            jsonArray = streamObj.getJSONArray("streams");
            firstStream = jsonArray.getJSONObject(0);
            channel = firstStream.getJSONObject("channel");
            streamTitle = channel.getString("name");
            System.out.println(streamTitle);
            return streamTitle;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(streamTitle);
        return streamTitle;
    }


    public String getStreamUrl(String streamName) {
        String token = "";
        String sig = "";
        InputStream is = null;
        String urlString = String.format("http://api.twitch.tv/api/channels/%s/access_token", streamName);
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "token":
                        token = reader.nextString();
                        break;
                    case "sig":
                        sig = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(DEBUG_TAG, "The TOKEN is: " + token);
        Log.d(DEBUG_TAG, "The SIG is: " + sig);

        try {
            token = URLEncoder.encode(token, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String streamURL = String.format("http://usher.twitch.tv/api/channel/hls/%1s.m3u8?token=%2s&sig=%3s", streamName, token, sig);
        Log.d(DEBUG_TAG, "The stream URL is: " + streamURL);
        return streamURL;
    }




    public JSONObject getSpecificStream(int streamNumber) {
        // calculate offset for stream url
        int offset = streamNumber - 1;
        InputStream is = null;
        String token = "";
        String sig = "";
        String urlString = BASE_STREAM_URL + "?limit=1&offset=" + Integer.toString(offset);
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response code is: " + response);
            is = conn.getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            responseObject = new JSONObject(responseStrBuilder.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(DEBUG_TAG, "The response is: " + responseObject.toString());
        return responseObject;
    }

    private static int getRandomNumberWithMax(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }
}
