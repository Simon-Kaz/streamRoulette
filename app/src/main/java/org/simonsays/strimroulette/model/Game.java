package org.simonsays.strimroulette.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szymonkaz on 29/05/2016.
 */

public class Game {

    @SerializedName("name")
    private String title;

    private int viewerCount;
    private int channelCount;
    private String thumbnailUrl;

    public Game(String name, int viewerCount, int channelCount, String thumbnailUrl) {
        this.title = name;
        this.viewerCount = viewerCount;
        this.channelCount = channelCount;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getViewerCount() {
        return viewerCount;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewerCount(int viewerCount) {
        this.viewerCount = viewerCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
