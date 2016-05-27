package org.simonsays.strimroulette;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;

import com.devbrackets.android.exomedia.EMVideoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class StreamActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private static final String DEBUG_TAG = "StreamActivityError";
    private static final String CHARSET = "UTF-8";
    private EMVideoView emVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EMVideoView emVideoView = (EMVideoView) findViewById(R.id.video_player);
        emVideoView.setOnPreparedListener(this);

        try {
            emVideoView.setVideoPath(new GetStreamURLTask().execute().get());
        } catch (InterruptedException | ExecutionException e) {
            Log.e(DEBUG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            emVideoView.start();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private class GetStreamURLTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... url) {
            return getStreamUrl("witwix");
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d(DEBUG_TAG, "The result is: " + result);
        }


        private String getStreamUrl(String channel) {
            String token = "";
            String sig = "";
            InputStream is = null;
            String urlString = String.format("http://api.twitch.tv/api/channels/%s/access_token", channel);
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
            String streamURL = String.format("http://usher.twitch.tv/api/channel/hls/%1s.m3u8?token=%2s&sig=%3s", channel, token, sig);
            Log.d(DEBUG_TAG, "The stream URL is: " + streamURL);
            return streamURL;

        }
    }
}
