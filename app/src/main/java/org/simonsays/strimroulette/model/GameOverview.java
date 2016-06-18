package org.simonsays.strimroulette.model;

import com.google.gson.annotations.SerializedName;

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

    public Integer getViewers() {
        return viewers;
    }

    public Integer getChannels() {
        return channels;
    }

    public GameDetails getGameDetails() {
        return gameDetails;
    }
}