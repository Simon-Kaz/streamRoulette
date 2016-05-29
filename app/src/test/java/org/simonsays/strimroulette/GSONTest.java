package org.simonsays.strimroulette;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.simonsays.strimroulette.utils.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szymonkaz on 27/05/2016.
 */

public class GSONTest {

    @Test
    public void simpleExample() {
        String json = "{\"brand\":\"Jeep\", \"doors\": 3}";

        Gson gson = new Gson();

        Car car = gson.fromJson(json, Car.class);
        System.out.println(car.toString());
        System.out.println(gson.toJson(car));
    }

    class Car {
        String brand;
        String doors;

        @Override
        public String toString() {
            return "Brand: " + brand + " Doors: " + doors;
        }
    }

    @Test
    public void storingResponseInGSON() {
        Gson gson = new Gson();

        Example example = gson.fromJson(gsonString, Example.class);
        Stream stream = gson.fromJson(gsonString, Stream.class);
        System.out.println(example.toString());
        System.out.println(stream.toString());


    }


    public class Example {

        public List<Stream> streams = new ArrayList<Stream>();
    }

    public class Stream {

        public Integer id;
        public String game;
        public Integer viewers;
        public String createdAt;
        public Integer videoHeight;
        public Integer averageFps;
        public Integer delay;
        public Boolean isPlaylist;

        @Override
        public String toString() {
            return id + game + viewers + createdAt + videoHeight + averageFps + delay + isPlaylist;
        }
    }

    String gsonString = "{\n" +
            "  \"streams\": [\n" +
            "    {\n" +
            "      \"_id\": 21535502816,\n" +
            "      \"game\": \"League of Legends\",\n" +
            "      \"viewers\": 21797,\n" +
            "      \"created_at\": \"2016-05-27T11:27:49Z\",\n" +
            "      \"video_height\": 720,\n" +
            "      \"average_fps\": 60,\n" +
            "      \"delay\": 0,\n" +
            "      \"is_playlist\": false,\n" +
            "      \"_links\": {\n" +
            "        \"self\": \"https://api.twitch.tv/kraken/streams/nightblue3\"\n" +
            "      },\n" +
            "      \"preview\": {\n" +
            "        \"small\": \"https://static-cdn.jtvnw.net/previews-ttv/live_user_nightblue3-80x45.jpg\",\n" +
            "        \"medium\": \"https://static-cdn.jtvnw.net/previews-ttv/live_user_nightblue3-320x180.jpg\",\n" +
            "        \"large\": \"https://static-cdn.jtvnw.net/previews-ttv/live_user_nightblue3-640x360.jpg\",\n" +
            "        \"template\": \"https://static-cdn.jtvnw.net/previews-ttv/live_user_nightblue3-{width}x{height}.jpg\"\n" +
            "      },\n" +
            "      \"channel\": {\n" +
            "        \"mature\": false,\n" +
            "        \"status\": \"Season 6 Jungle Patch 6.10 - CHALLENGER - Jungle Variety - UNLEASH YOUR INNER JUNGLER SPIRIT - WEEB FRIENDLY STREAM VoHiYo\",\n" +
            "        \"broadcaster_language\": \"en\",\n" +
            "        \"display_name\": \"Nightblue3\",\n" +
            "        \"game\": \"League of Legends\",\n" +
            "        \"language\": \"en\",\n" +
            "        \"_id\": 26946000,\n" +
            "        \"name\": \"nightblue3\",\n" +
            "        \"created_at\": \"2011-12-21T18:18:40Z\",\n" +
            "        \"updated_at\": \"2016-05-27T13:03:48Z\",\n" +
            "        \"delay\": null,\n" +
            "        \"logo\": \"https://static-cdn.jtvnw.net/jtv_user_pictures/nightblue3-profile_image-be8a5ea2b11d7f12-300x300.png\",\n" +
            "        \"banner\": null,\n" +
            "        \"video_banner\": \"https://static-cdn.jtvnw.net/jtv_user_pictures/nightblue3-channel_offline_image-4cc024a7516eedf4-1920x1080.jpeg\",\n" +
            "        \"background\": null,\n" +
            "        \"profile_banner\": \"https://static-cdn.jtvnw.net/jtv_user_pictures/nightblue3-profile_banner-1d7d926bdc159d8b-480.png\",\n" +
            "        \"profile_banner_background_color\": null,\n" +
            "        \"partner\": true,\n" +
            "        \"url\": \"https://www.twitch.tv/nightblue3\",\n" +
            "        \"views\": 135726091,\n" +
            "        \"followers\": 1350977,\n" +
            "        \"_links\": {\n" +
            "          \"self\": \"https://api.twitch.tv/kraken/channels/nightblue3\",\n" +
            "          \"follows\": \"https://api.twitch.tv/kraken/channels/nightblue3/follows\",\n" +
            "          \"commercial\": \"https://api.twitch.tv/kraken/channels/nightblue3/commercial\",\n" +
            "          \"stream_key\": \"https://api.twitch.tv/kraken/channels/nightblue3/stream_key\",\n" +
            "          \"chat\": \"https://api.twitch.tv/kraken/chat/nightblue3\",\n" +
            "          \"features\": \"https://api.twitch.tv/kraken/channels/nightblue3/features\",\n" +
            "          \"subscriptions\": \"https://api.twitch.tv/kraken/channels/nightblue3/subscriptions\",\n" +
            "          \"editors\": \"https://api.twitch.tv/kraken/channels/nightblue3/editors\",\n" +
            "          \"teams\": \"https://api.twitch.tv/kraken/channels/nightblue3/teams\",\n" +
            "          \"videos\": \"https://api.twitch.tv/kraken/channels/nightblue3/videos\"\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"_total\": 15193,\n" +
            "  \"_links\": {\n" +
            "    \"self\": \"https://api.twitch.tv/kraken/streams?limit=1&offset=0\",\n" +
            "    \"next\": \"https://api.twitch.tv/kraken/streams?limit=1&offset=1\",\n" +
            "    \"featured\": \"https://api.twitch.tv/kraken/streams/featured\",\n" +
            "    \"summary\": \"https://api.twitch.tv/kraken/streams/summary\",\n" +
            "    \"followed\": \"https://api.twitch.tv/kraken/streams/followed\"\n" +
            "  }\n" +
            "}";
}
