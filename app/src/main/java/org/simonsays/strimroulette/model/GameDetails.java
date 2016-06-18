package org.simonsays.strimroulette.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szymonkaz on 16/06/16.
 */
public class GameDetails {

    public String name;

    @SerializedName("box")
    public Box box;

    @SerializedName("logo")
    public Logo logo;

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
