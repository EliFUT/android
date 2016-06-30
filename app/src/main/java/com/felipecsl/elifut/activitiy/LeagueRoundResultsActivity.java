package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.RoundResultsAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;

public class LeagueRoundResultsActivity extends ElifutActivity {
  private static final String EXTRA_ROUND = "EXTRA_ROUND";

  @BindView(R.id.recycler_round_results) RecyclerView recyclerRoundResults;
  @BindView(R.id.toolbar) Toolbar toolbar;

  @State LeagueRound round;

  public static Intent newIntent(Context context, LeagueRound leagueRound) {
    return new Intent(context, LeagueRoundResultsActivity.class)
        .putExtra(EXTRA_ROUND, leagueRound);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_league_round_results);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      round = intent.getParcelableExtra(EXTRA_ROUND);
    }

    //noinspection ConstantConditions
    getSupportActionBar().setTitle(getString(R.string.round_results,
        String.valueOf(round.roundNumber())));

    Club currentClub = userPreferences.club();
    LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerRoundResults.setLayoutManager(layout);
    recyclerRoundResults.setAdapter(new RoundResultsAdapter(currentClub, round.matches()));
  }

  @OnClick(R.id.fab) public void onClickFab() {
    finish();
  }
}
