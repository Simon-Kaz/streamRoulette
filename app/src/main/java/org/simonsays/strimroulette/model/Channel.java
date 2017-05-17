package org.simonsays.strimroulette.model;

/**
 * Created by szymonkaz on 05/06/16.
 */
public class Channel {

    private String status;
    private String broadcasterLanguage;
    private String display_name;
    private String game;
    private String language;
    private String name;
    private String logo;
    private String video_banner;
    private String profile_banner;
    private Boolean partner;
    private String url;
    private Integer followers;

    @Override
    public String toString() {
        return display_name + " broadcasting " + game;
    }

    public String getStatus() {
        return status;
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }
}