package com.felipecsl.elifut.activitiy;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.LeagueMatchesAdapter;
import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.preferences.UserPreferences;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;
import rx.Observable;

public class LeagueRoundResultsActivity extends ElifutActivity {
  private static final String EXTRA_ROUND = "EXTRA_ROUND";

  @Bind(R.id.recycler_round_results) RecyclerView roundResults;

  @Inject UserPreferences userPreferences;

  @State LeagueRound round;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_league_round_results);
    ButterKnife.bind(this);

    if (savedInstanceState == null) {
      round = getIntent().getParcelableExtra(EXTRA_ROUND);
    }

    MatchResultGenerator generator = new MatchResultGenerator();
    Observable<MatchResult> observable = Observable.from(round.matches()).map(generator::generate);

    Club currentClub = userPreferences.clubPreference().get();
    roundResults.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    String headerText = getString(R.string.round_results, round.roundNumber());
    roundResults.setAdapter(new LeagueMatchesAdapter(currentClub, headerText, round.matches()));
  }
}