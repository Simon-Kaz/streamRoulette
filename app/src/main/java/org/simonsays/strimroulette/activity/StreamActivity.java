package org.simonsays.strimroulette.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.simonsays.strimroulette.R;
import org.simonsays.strimroulette.model.AccessTokenResp;
import org.simonsays.strimroulette.model.Stream;
import org.simonsays.strimroulette.model.TopStreamsResp;
import org.simonsays.strimroulette.rest.ApiClient;
import org.simonsays.strimroulette.rest.TwitchService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Loading...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        likedList = new ArrayList<>();

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
                chainedCallExample(rand);
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
                twitchService.specificStreamResp(randNumber, "en");

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
                    game_title_textView = (TextView) findViewById(R.id.game_title_textView);
                    game_title_textView.setText(streamResult.game);

                    Stream.Channel channelResult = streamResult.getChannel();
                    stream_title_textView = (TextView) findViewById(R.id.title_textView);
                    stream_title_textView.setText(channelResult.status);

                    channel_name_textView = (TextView) findViewById(R.id.channel_name_textView);
                    channel_name_textView.setText(channelResult.display_name);
                    toolbar.setTitle(channelResult.display_name);

                    viewer_count_textView = (TextView) findViewById(R.id.viewer_count_textView);
                    viewer_count_textView.setText(streamResult.viewers.toString());

                    channel_logo_imageView = (ImageView) findViewById(R.id.channel_logo);
                    Picasso
                            .with(getApplicationContext())
                            .load(channelResult.logo)
                            .fit()
                            .into(channel_logo_imageView);

                    //get stream url
                    final String channelName = channelResult.name;
                    final Call<AccessTokenResp> accessTokenCall =
                            twitchService.accessTokenResp(channelName);

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

    private void showAddedToLikedSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), "Channel added to Liked List!", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.GREEN)
                .show();
    }

    private void showChannelAlreadyAddedSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), "Channel already in the Liked List!", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.GREEN)
                .show();
    }

    private static int getRandomNumberWithMax(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }
}
