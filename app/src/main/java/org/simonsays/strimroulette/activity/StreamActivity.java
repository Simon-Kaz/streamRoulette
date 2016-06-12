package org.simonsays.strimroulette.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.google.gson.Gson;

import org.simonsays.strimroulette.R;
import org.simonsays.strimroulette.model.AccessTokenResp;
import org.simonsays.strimroulette.model.GameOverview;
import org.simonsays.strimroulette.model.Stream;
import org.simonsays.strimroulette.model.TopStreamsResp;
import org.simonsays.strimroulette.rest.ApiClient;
import org.simonsays.strimroulette.rest.TwitchService;
import org.simonsays.strimroulette.utils.http.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private static final String DEBUG_TAG = StreamActivity.class.getSimpleName();
    private EMVideoView emVideoView;
    private HttpUtils httpUtils;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        httpUtils = new HttpUtils();

        emVideoView = (EMVideoView) findViewById(R.id.video_player);
        emVideoView.setOnPreparedListener(this);
        try {
            int rand = getRandomNumberWithMax(2000);
            chainedCallExample(rand);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        progressBar.setVisibility(View.GONE);
        emVideoView.requestFocus();
        emVideoView.start();
        Log.d(DEBUG_TAG, String.valueOf(emVideoView.getVideoUri()));
    }

    public void chainedCallExample(int randNumber) {

        final TwitchService twitchService = ApiClient.getClient().create(TwitchService.class);
        final Call<TopStreamsResp> streamDataCall =
                twitchService.specificStreamResp(randNumber, null);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        streamDataCall.enqueue(new Callback<TopStreamsResp>() {
            @Override
            public void onResponse(Call<TopStreamsResp> call, Response<TopStreamsResp> response) {
                if (response.isSuccessful()) {

                    // store response body in result
                    TopStreamsResp result = response.body();
                    Log.d(DEBUG_TAG, "FIRST CALL RESULT = " + new Gson().toJson(result));

                    // get first (only expecting one) Stream object
                    Stream streamResult = result.getStream(0);
                    final TextView textView = (TextView) findViewById(R.id.game_title_textView);
                    textView.setText(streamResult.game);

                    Stream.Channel channelResult = streamResult.getChannel();
                    final TextView textView2 = (TextView) findViewById(R.id.title_textView);
                    textView2.setText(channelResult.status);

                    final TextView textView3 = (TextView) findViewById(R.id.channel_name_textView);
                    textView3.setText(channelResult.display_name);

                    final TextView textView4 = (TextView) findViewById(R.id.viewer_count_textView);
                    textView4.setText("Current Viewers: " + streamResult.viewers);

                    final String channelName = channelResult.name;

                    final Call<AccessTokenResp> accessTokenCall =
                            twitchService.accessTokenResp(channelName);

                    // get stream url
                    accessTokenCall.enqueue(new Callback<AccessTokenResp>() {
                        @Override
                        public void onResponse(Call<AccessTokenResp> call, Response<AccessTokenResp> response) {
                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {
                                AccessTokenResp accessTokenResp = response.body();
                                Log.d(DEBUG_TAG, "SECOND CALL RESULT = " + new Gson().toJson(accessTokenResp));
                                String sig = accessTokenResp.sig;
                                String token = accessTokenResp.token;

                                // URL encode the token
                                try {
                                    token = URLEncoder.encode(token, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                String streamURL = String.format("http://usher.twitch.tv/api/channel/hls/%1s.m3u8?token=%2s&sig=%3s", channelName, token, sig);
                                Log.d(DEBUG_TAG, "FINAL URL: " + streamURL);

                                emVideoView.setVideoPath(streamURL);


                            } else {
                                showErrorSnackbar();
                            }
                        }

                        @Override
                        public void onFailure(Call<AccessTokenResp> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            showErrorSnackbar();
                        }
                    });


                } else {
                    showErrorSnackbar();
                }
            }

            @Override
            public void onFailure(Call<TopStreamsResp> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorSnackbar();
            }
        });
    }

    private void showErrorSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), "Request failed", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();
    }

    private static int getRandomNumberWithMax(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }
}
