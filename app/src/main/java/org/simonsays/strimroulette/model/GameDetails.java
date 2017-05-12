package org.simonsays.strimroulette.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szymonkaz on 16/06/16.
 */
public class GameDetails {

    private String name;

    @SerializedName("box")
    private Box box;

    @SerializedName("logo")
    private Logo logo;

    public String getName() {
        return name;
    }

    public Box getBox() {
        return box;
    }

    public Logo getLogo() {
        return logo;
    }
}
