package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.LeagueNextMatchesAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.preferences.LeaguePreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;

public class LeagueProgressActivity extends ElifutActivity {
  private static final String EXTRA_LEAGUE = "EXTRA_LEAGUE";
  private static final String EXTRA_CURRENT_CLUB = "EXTRA_CURRENT_CLUB";
  private static final String EXTRA_OPPONENTS = "EXTRA_OPPONENTS";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.recycler_next_matches) RecyclerView recyclerView;

  @Inject LeaguePreferences leaguePreferences;

  @State ArrayList<Club> nextOpponents;
  @State Club currentClub;
  @State League league;

  public static Intent newIntent(
      Context context, Club currentClub, League league, List<Club> nextOpponents) {
    return new Intent(context, LeagueProgressActivity.class)
        .putExtra(EXTRA_LEAGUE, league)
        .putExtra(EXTRA_CURRENT_CLUB, currentClub)
        .putParcelableArrayListExtra(EXTRA_OPPONENTS, new ArrayList<>(nextOpponents));
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_league_progress);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      league = intent.getParcelableExtra(EXTRA_LEAGUE);
      currentClub = intent.getParcelableExtra(EXTRA_CURRENT_CLUB);
      nextOpponents = intent.getParcelableArrayListExtra(EXTRA_OPPONENTS);
    }

    LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layout);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(new LeagueNextMatchesAdapter(nextOpponents, currentClub));
  }

  @OnClick(R.id.fab) public void onClickNext() {
    Club nextOpponent = nextOpponents.get(0);
    startActivity(MatchProgressActivity.newIntent(this, currentClub, nextOpponent));
    finish();
  }
}
