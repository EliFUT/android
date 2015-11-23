package com.felipecsl.elifut.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.LeagueNextMatchesAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;

public class LeagueProgressFragment extends ElifutFragment {
  @Bind(R.id.recycler_next_matches) RecyclerView recyclerView;

  @State ArrayList<Club> nextOpponents;
  @State Club currentClub;
  @State League league;

  public static LeagueProgressFragment newInstance() {
    return new LeagueProgressFragment();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_league_progress, container, false);
    ButterKnife.bind(this, view);
    if (savedInstanceState == null) {
      league = userPreferences.leaguePreference().get();
      currentClub = userPreferences.clubPreference().get();
      nextOpponents = new ArrayList<>(leaguePreferences.opponentsPreference().get());
    }

    LinearLayoutManager layout = new LinearLayoutManager(
        getActivity(), LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layout);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(new LeagueNextMatchesAdapter(nextOpponents, currentClub));

    return view;
  }
}
