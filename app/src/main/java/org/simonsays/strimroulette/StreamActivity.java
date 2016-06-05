package org.simonsays.strimroulette;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.devbrackets.android.exomedia.EMVideoView;

import org.simonsays.strimroulette.utils.http.HttpUtils;

import java.util.concurrent.ExecutionException;

public class StreamActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private static final String DEBUG_TAG = StreamActivity.class.getSimpleName();
    private EMVideoView emVideoView;
    private HttpUtils httpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        httpUtils = new HttpUtils();

        EMVideoView emVideoView = (EMVideoView) findViewById(R.id.video_player);
        if (emVideoView != null) {
            emVideoView.setOnPreparedListener(this);
        }

        try {
            if (emVideoView != null) {
                emVideoView.setVideoPath(new GetStreamURLTask().execute().get());
            }
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
            return httpUtils.getRandomStrim();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d(DEBUG_TAG, "The result is: " + result);
        }



    }
}
