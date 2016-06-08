package org.simonsays.strimroulette.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szymonkaz on 07/06/16.
 */
public class GameOverview {

    public Integer viewers;
    public Integer channels;

    @SerializedName("game")
    public GameDetails gameDetails;

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public void setGameDetails(GameDetails gameDetails) {
        this.gameDetails = gameDetails;
    }

    public class GameDetails {

        public String name;

        @SerializedName("box")
        public Box box;

        @SerializedName("logo")
        public Logo logo;

    }

    public class Box {

        public String large;
        public String medium;
        public String small;
        public String template;

    }

    public class Logo {

        public String large;
        public String medium;
        public String small;
        public String template;

    }
}