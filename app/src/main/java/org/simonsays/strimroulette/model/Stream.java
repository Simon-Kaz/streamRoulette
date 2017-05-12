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

    public class Channel {

        private Boolean mature;
        private String status;
        private String broadcaster_language;
        private String display_name;
        private String game;
        private String language;
        private Integer id;
        private String name;
        private String created_at;
        private String updated_at;
        private String logo;
        private String video_banner;
        private String profile_banner;
        private String profile_banner_background_color;
        private Boolean partner;
        private String url;
        private Integer views;
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
}