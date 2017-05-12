package org.simonsays.strimroulette.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.simonsays.strimroulette.R;
import org.simonsays.strimroulette.model.AccessTokenResponse;
import org.simonsays.strimroulette.model.Stream;
import org.simonsays.strimroulette.model.TopStreamsResponse;
import org.simonsays.strimroulette.rest.ApiClient;
import org.simonsays.strimroulette.rest.TwitchService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = StreamActivity.class.getSimpleName();
    private EMVideoView emVideoView;
    private Toolbar toolbar;
    private TextView viewer_count_textView;
    private TextView channel_name_textView;
    private TextView game_title_textView;
    private TextView stream_title_textView;
    private ProgressBar progressBar;
    private ImageView channel_logo_imageView;
    private ArrayList<String> likedList;
    private LinearLayout streamInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Loading...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        likedList = new ArrayList<>();
        streamInfo = (LinearLayout) findViewById(R.id.stream_info);

        emVideoView = (EMVideoView) findViewById(R.id.video_player);
        emVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                progressBar.setVisibility(View.GONE);
                emVideoView.requestFocus();
                emVideoView.start();
            }
        });

        try {
            int rand = getRandomNumberWithMax(2000);
            getChannelData(rand);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            try {
                int rand = getRandomNumberWithMax(2000);
                getChannelData(rand);
            } catch (Exception e) {
                Log.e(DEBUG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        if (id == R.id.action_like) {
            String channelName = (String) channel_name_textView.getText();
            if (likedList.contains(channelName)) {
                showChannelAlreadyAddedSnackbar();
            } else {
                likedList.add(channelName);
                showAddedToLikedSnackbar();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getChannelData(int randNumber) {

        final TwitchService twitchService = ApiClient.getClient().create(TwitchService.class);
        final Call<TopStreamsResponse> streamDataCall =
                twitchService.specificStreamResponse(randNumber, "en");

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // hide stream info, show progress bar
        streamInfo.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        streamDataCall.enqueue(new Callback<TopStreamsResponse>() {
            @Override
            public void onResponse(Call<TopStreamsResponse> call, Response<TopStreamsResponse> response) {
                if (response.isSuccessful()) {

                    // store response body in result
                    TopStreamsResponse result = response.body();
                    Log.d(DEBUG_TAG, "Stream Data Call Result= " + new Gson().toJson(result));

                    game_title_textView = (TextView) findViewById(R.id.game_title_textView);
                    stream_title_textView = (TextView) findViewById(R.id.title_textView);
                    channel_name_textView = (TextView) findViewById(R.id.channel_name_textView);
                    viewer_count_textView = (TextView) findViewById(R.id.viewer_count_textView);
                    channel_logo_imageView = (ImageView) findViewById(R.id.channel_logo);

                    // Grabbing the first stream
                    Stream streamResult = result.getStream(0);
                    Stream.Channel channelResult = streamResult.getChannel();

                    game_title_textView.setText(streamResult.getGame());
                    stream_title_textView.setText(channelResult.getStatus());
                    channel_name_textView.setText(channelResult.getDisplayName());
                    toolbar.setTitle(channelResult.getDisplayName());
                    viewer_count_textView.setText(streamResult.getViewers().toString());

                    Picasso
                            .with(getApplicationContext())
                            .load(channelResult.getLogo())
                            .fit()
                            .into(channel_logo_imageView);

                    //get stream url
                    final String channelName = channelResult.getName();

                    final Call<AccessTokenResponse> accessTokenCall =
                            twitchService.accessTokenResponse(channelName);

                    accessTokenCall.enqueue(new Callback<AccessTokenResponse>() {
                        @Override
                        public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {
                                AccessTokenResponse accessTokenResponse = response.body();
                                Log.d(DEBUG_TAG, "Access Token Call Result= " + new Gson().toJson(accessTokenResponse));
                                String sig = accessTokenResponse.sig;
                                String token = accessTokenResponse.token;

                                // URL encode the token
                                try {
                                    token = URLEncoder.encode(token, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                String streamURL = String.format("http://usher.twitch.tv/api/channel/hls/%1s.m3u8?token=%2s&sig=%3s", channelName, token, sig);
                                Log.d(DEBUG_TAG, "FINAL video url: " + streamURL);

                                emVideoView.setVideoPath(streamURL);
                                streamInfo.setVisibility(View.VISIBLE);
                            } else {
                                showErrorSnackbar();
                            }
                        }

                        @Override
                        public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            showErrorSnackbar();
                        }
                    });

                } else {
                    showErrorSnackbar();
                }
            }

            @Override
            public void onFailure(Call<TopStreamsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorSnackbar();
            }
        });
    }

    private void createSnackbar(String textToDisplay, @ColorInt int color) {
        Snackbar.make(findViewById(android.R.id.content), textToDisplay, Snackbar.LENGTH_LONG)
                .setActionTextColor(color)
                .show();
    }

    private void showErrorSnackbar() {
        createSnackbar("Request Failed", Color.RED);
    }

    private void showAddedToLikedSnackbar() {
        createSnackbar("Channel added to Liked List!", Color.GREEN);
    }

    private void showChannelAlreadyAddedSnackbar() {
        createSnackbar("Channel already in the Liked List!", Color.GREEN);
    }

    private static int getRandomNumberWithMax(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }

    private void enterFullscreenVideo() {
        emVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
}