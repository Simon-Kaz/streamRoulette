package org.simonsays.strimroulette.rest;

import org.simonsays.strimroulette.model.AccessTokenResponse;
import org.simonsays.strimroulette.model.TopGamesResponse;
import org.simonsays.strimroulette.model.TopStreamsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitchService {

    // get stream data for specified channel
    @GET("streams/{channel}")
    Call<TopStreamsResponse> channelData(
            @Path("channel") String channel
    );

    // get stream data for stream using offset
    @GET("streams?limit=1")
    Call<TopStreamsResponse> specificStreamResponse(
            @Query("offset") int offset,
            @Query("language") String language
    );

    // get top games (max viewers DESC)
    @GET("games/top")
    Call<TopGamesResponse> topGamesResponse(
            @Query("limit") int limit
    );

    // get access token for video streaming
    @GET("http://api.twitch.tv/api/channels/{channel}/access_token")
    Call<AccessTokenResponse> accessTokenResponse(
            @Path("channel") String channel
    );
}