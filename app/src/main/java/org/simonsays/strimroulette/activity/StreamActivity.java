package org.simonsays.strimroulette.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
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
import org.simonsays.strimroulette.model.Channel;
import org.simonsays.strimroulette.model.Stream;
import org.simonsays.strimroulette.model.TopStreamsResponse;
import org.simonsays.strimroulette.rest.ApiClient;
import org.simonsays.strimroulette.rest.TwitchService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = StreamActivity.class.getSimpleName();
    @BindView(R.id.video_player) private EMVideoView emVideoView;
    @BindView(R.id.tool_bar) private Toolbar toolbar;
    @BindView(R.id.progress_bar) private ProgressBar progressBar;
    @BindView(R.id.stream_info) private LinearLayout streamInfo;
    @BindView(R.id.game_title_textView) private TextView game_title_textView;
    @BindView(R.id.title_textView) private TextView stream_title_textView;
    @BindView(R.id.channel_name_textView) private TextView channel_name_textView;
    @BindView(R.id.viewer_count_textView) private TextView viewer_count_textView;

    private ArrayList<String> likedList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.loading_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                progressBar.setVisibility(View.GONE);
                emVideoView.requestFocus();
                emVideoView.start();
            }
        });
        populateVideo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            populateVideo();
        } else if (id == R.id.action_like) {
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

    public void populateVideo() {
        // Generate a random number up to 2k, as there are usually less than 2k streamers
        int rand = getRandomNumberWithMax(2000);

        final TwitchService twitchService = ApiClient.getClient().create(TwitchService.class);
        final Call<TopStreamsResponse> streamDataCall =
                twitchService.specificStreamResponse(rand, "en");

        // hide stream info, show progress bar
        streamInfo.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        streamDataCall.enqueue(getStreamDataCallback(twitchService));
    }

    @NonNull
    private Callback<TopStreamsResponse> getStreamDataCallback(final TwitchService twitchService) {
        return new Callback<TopStreamsResponse>() {
            @Override
            public void onResponse(Call<TopStreamsResponse> call, Response<TopStreamsResponse> response) {
                if (response.isSuccessful()) {
                    // store response body in result
                    TopStreamsResponse result = response.body();
                    Log.d(DEBUG_TAG, "Stream Data Call Result= " + new Gson().toJson(result));

                    // Grabbing the first stream result
                    Stream stream = result.getStream(0);
                    populateTextFields(stream);

                    Channel channel = stream.getChannel();
                    displayChannelLogo(channel.getLogo());

                    // Get an access token to display the video
                    final Call<AccessTokenResponse> accessTokenCall =
                            twitchService.accessTokenResponse(channel.getName());

                    accessTokenCall.enqueue(getTokenCallback(channel.getName()));

                } else {
                    showErrorSnackbar();
                }
            }

            @Override
            public void onFailure(Call<TopStreamsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorSnackbar();
            }
        };
    }

    private void displayChannelLogo(String logo) {
        ImageView channel_logo_imageView = (ImageView) findViewById(R.id.channel_logo);

        Picasso
                .with(getApplicationContext())
                .load(logo)
                .fit()
                .into(channel_logo_imageView);
    }

    private void populateTextFields(Stream stream) {
        Channel channel = stream.getChannel();

        game_title_textView.setText(stream.getGame());
        stream_title_textView.setText(channel.getStatus());
        channel_name_textView.setText(channel.getDisplayName());
        toolbar.setTitle(channel.getDisplayName());
        viewer_count_textView.setText(stream.getViewers().toString());
    }

    @NonNull
    private Callback<AccessTokenResponse> getTokenCallback(final String channelName) {
        return new Callback<AccessTokenResponse>() {
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
        };
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