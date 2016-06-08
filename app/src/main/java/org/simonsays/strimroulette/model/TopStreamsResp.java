package org.simonsays.strimroulette.model;

import java.util.List;

/**
 * Created by szymonkaz on 07/06/16.
 */
public class TopStreamsResp {

    private List<Stream> streams;
    private int _total;

    public List<Stream> getStreams() {
        return streams;
    }

    public Stream getStream(int i) {
        return streams.get(0);
    }

}

