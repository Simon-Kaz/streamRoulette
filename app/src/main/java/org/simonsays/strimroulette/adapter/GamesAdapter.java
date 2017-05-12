package org.simonsays.strimroulette.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.simonsays.strimroulette.model.Game;
import org.simonsays.strimroulette.R;

import java.util.List;

/**
 * Created by szymonkaz on 29/05/2016.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Game> gameList;

    public GamesAdapter(Context mContext, List<Game> gameList) {
        this.mContext = mContext;
        this.gameList = gameList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, viewerCount, channelCount;
        private ImageView thumbnail;


        private MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            viewerCount = (TextView) view.findViewById(R.id.viewer_count);
            channelCount = (TextView) view.findViewById(R.id.channel_count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.title.setText(game.getTitle());
        holder.viewerCount.setText(String.valueOf(game.getViewerCount()));
        holder.channelCount.setText(String.valueOf(game.getChannelCount()));

        Picasso
                .with(mContext)
                .load(game.getThumbnailUrl())
                .fit()
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
