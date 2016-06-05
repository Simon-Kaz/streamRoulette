package org.simonsays.strimroulette;

/**
 * Created by szymonkaz on 29/05/2016.
 */

public class Game {
    private String name;
    private int numOfViewers;
    private int numOfChannels;
    private int thumbnail;

    public Game(){
    }

    public Game(String name, int numOfViewers, int numOfChannels, int thumbnail) {
        this.name = name;
        this.numOfViewers = numOfViewers;
        this.numOfChannels = numOfChannels;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public int getNumOfViewers() {
        return numOfViewers;
    }

    public int getNumOfChannels() {
        return numOfChannels;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumOfViewers(int numOfViewers) {
        this.numOfViewers = numOfViewers;
    }

    public void setNumOfChannels(int numOfChannels) {
        this.numOfChannels = numOfChannels;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
