package org.simonsays.strimroulette.model;

/**
 * Created by szymonkaz on 07/06/16.
 */
public class Stream {

    private String game;
    private Integer viewers;
    private Integer video_height;
    private Float average_fps;
    private Channel channel;

    public String getGame() {
        return game;
    }

    public Integer getViewers() {
        return viewers;
    }

    public Integer getVideo_height() {
        return video_height;
    }

    public Float getAverage_fps() {
        return average_fps;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return game + " broadcasted for " + viewers;
    }
}