package com.felipecsl.elifut.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.SimpleResponseObserver;
import com.felipecsl.elifut.adapter.PlayersAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.util.FragmentBundler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public final class TeamPlayersFragment extends ElifutFragment {
  private static final String TAG = TeamPlayersFragment.class.getSimpleName();
  private static final String EXTRA_CLUB = "EXTRA_CLUB";

  @Bind(R.id.progress_bar_layout) ViewGroup progressBarLayout;
  @Bind(R.id.recycler_players) RecyclerView playersList;
  @BindDimen(R.dimen.player_spacing) int playerSpacing;

  @State Club club;
  @State Nation nation;
  @State ArrayList<Player> players;

  public static TeamPlayersFragment newInstance(Club club) {
    return FragmentBundler.make(new TeamPlayersFragment())
        .putParcelable(EXTRA_CLUB, club)
        .build();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_team_players, container, false);

    ButterKnife.bind(this, v);
    Icepick.restoreInstanceState(this, savedInstanceState);
    daggerComponent().inject(this);

    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    playersList.setLayoutManager(layoutManager);
    playersList.setHasFixedSize(true);

    if (savedInstanceState == null) {
      club = getArguments().getParcelable(EXTRA_CLUB);
      loadPlayers();
    } else {
      onPlayersLoaded();
    }

    return v;
  }

  private void onPlayersLoaded() {
    progressBarLayout.setVisibility(View.GONE);
    playersList.setAdapter(new PlayersAdapter(players, club));
  }

  private void loadPlayers() {
    service.playersByClub(club.id())
        .compose(this.<List<Player>>applyTransformations())
        .subscribe(new SimpleResponseObserver<List<Player>>() {
          @Override public void onError(Throwable e) {
            Toast.makeText(getActivity(), "Failed to load club players", Toast.LENGTH_SHORT).show();
            Log.w(TAG, e);
          }

          @Override public void onNext(List<Player> response) {
            players = new ArrayList<>(response);
            onPlayersLoaded();
          }
        });
  }
}
