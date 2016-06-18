package org.simonsays.strimroulette.fragment;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.simonsays.strimroulette.R;
import org.simonsays.strimroulette.adapter.GamesAdapter;
import org.simonsays.strimroulette.model.Game;
import org.simonsays.strimroulette.model.GameDetails;
import org.simonsays.strimroulette.model.GameOverview;
import org.simonsays.strimroulette.model.TopGamesResp;
import org.simonsays.strimroulette.rest.ApiClient;
import org.simonsays.strimroulette.rest.TwitchService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesFragment extends Fragment {

    private RecyclerView recyclerView;
    private GamesAdapter adapter;
    private List<Game> gameList;
    private View rootView;
    private ProgressBar progressBar;

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_games, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadGames();
        return rootView;
    }


//    private void prepareGames() {
//        String[] covers = new String[]{
//                R.drawable.thumb1,
//                R.drawable.thumb2,
//                R.drawable.thumb3,
//                R.drawable.thumb4,
//                R.drawable.thumb5,
//                R.drawable.thumb6};
//
//        Game a = new Game("League Of Legends", 113888, 1509,covers[0]);
//        gameList.add(a);
//
//        a = new Game("Hearthstone: Heroes of Warcraft", 78785, 295,covers[1]);
//        gameList.add(a);
//
//        a = new Game("Overwatch", 60060, 1580,covers[2]);
//        gameList.add(a);
//
//        a = new Game("Counter Strike: Global Offensive", 46164, 916,covers[3]);
//        gameList.add(a);
//
//        a = new Game("Dota 2", 38674, 497,covers[4]);
//        gameList.add(a);
//
//        a = new Game("Call of Duty: Black Ops III", 12448, 1052,covers[5]);
//        gameList.add(a);
//
//        adapter.notifyDataSetChanged();
//    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    public void loadGames() {
        TwitchService twitchService = ApiClient.getClient().create(TwitchService.class);
        final Call<TopGamesResp> topGamesCall =
                twitchService.topGamesResp(100);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        topGamesCall.enqueue(new Callback<TopGamesResp>() {
            @Override
            public void onResponse(Call<TopGamesResp> call, Response<TopGamesResp> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    TopGamesResp topGamesResp = response.body();
                    List<GameOverview> gameOverviewList = topGamesResp.getGameOverview();
                    gameList = new ArrayList<>();

                    for (GameOverview gameOverview: gameOverviewList) {
                        int channelCount = gameOverview.getChannels();
                        int viewerCount = gameOverview.getViewers();

                        // get inner json for game name and thumbnail url
                        GameDetails gameDetails = gameOverview.getGameDetails();

                        String gameName = gameDetails.getName();
                        String thumbnailUrl = gameDetails.getBox().getLarge();

                        Game game = new Game(gameName, viewerCount, channelCount, thumbnailUrl);
                        gameList.add(game);
                    }
                    adapter = new GamesAdapter(getContext(), gameList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TopGamesResp> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorSnackbar();
            }
        });
    }


    private void showErrorSnackbar() {
        Snackbar.make(rootView.findViewById(android.R.id.content), "Request failed", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();
    }

}
