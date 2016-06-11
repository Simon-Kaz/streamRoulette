package org.simonsays.strimroulette.rest;

import org.simonsays.strimroulette.model.AccessTokenResp;
import org.simonsays.strimroulette.model.TopGamesResp;
import org.simonsays.strimroulette.model.TopStreamsResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitchService {

    // get stream data for specified channel
    @GET("streams/{channel}")
    Call<TopStreamsResp> channelData(
            @Path("channel") String channel
    );

    // get stream data for stream using offset
    @GET("streams?limit=1")
    Call<TopStreamsResp> specificStreamResp(
            @Query("offset") int offset
    );

    // get top games (max viewers DESC)
    @GET("games/top")
    Call<TopGamesResp> topGamesResp();

    // get access token for video streaming
    @GET("http://api.twitch.tv/api/channels/{channel}/access_token")
    Call<AccessTokenResp> accessTokenResp(
            @Path("channel") String channel
    );

//    // get streaming video
//    @GET("http://usher.twitch.tv/api/channel/hls/{channel}.m3u8?token=%2s&sig=%3s")
//    Call<ResponseBody> getVideoStream(
//            @Path("channel") String channel,
//            @Query("token") String token,
//            @Query("sig") String sig
//    );
}