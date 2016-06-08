package org.simonsays.strimroulette.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by szymonkaz on 07/06/16.
 */
public class TopGamesResp {

    public Integer _total;

    @SerializedName("top")
    public List<GameOverview> gameOverview;

    @Override
    public String toString() {
        return _total.toString();
    }

    public List<GameOverview> getGameOverview() {
        return gameOverview;
    }

}
