package org.simonsays.strimroulette.model;

/**
 * Created by szymonkaz on 05/06/16.
 */
public class Channel {

    public String status;
    public String broadcasterLanguage;
    public String display_name;
    public String game;
    public String language;
    public String name;
    public String logo;
    public String video_banner;
    public String profile_banner;
    public Boolean partner;
    public String url;
    public Integer followers;

    @Override
    public String toString() {
        return display_name + " broadcasting " + game;
    }
}