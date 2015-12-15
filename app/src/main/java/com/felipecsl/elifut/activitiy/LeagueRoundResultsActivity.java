package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.RoundResultsAdapter;
import com.felipecsl.elifut.match.LeagueRoundExecutor;
import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.MatchResult;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;
import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.toList;

public class LeagueRoundResultsActivity extends ElifutActivity {
  private static final String EXTRA_ROUND = "EXTRA_ROUND";

  @Bind(R.id.recycler_round_results) RecyclerView roundResults;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @State LeagueRound round;

  @Inject LeagueRoundExecutor executor;

  private final MatchResultGenerator resultGenerator = new MatchResultGenerator();

  public static Intent newIntent(Context context, LeagueRound leagueRound) {
    return new Intent(context, LeagueRoundResultsActivity.class)
        .putExtra(EXTRA_ROUND, leagueRound);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_league_round_results);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      round = getIntent().getParcelableExtra(EXTRA_ROUND);
    }

    getSupportActionBar().setTitle(getString(R.string.round_results, round.roundNumber()));

    Observable<MatchResult> resultsObservable = Observable.from(round.matches())
        .map(resultGenerator::generate);
    executor.execute(resultsObservable);

    Club currentClub = userPreferences.club();
    roundResults.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    roundResults.setAdapter(new RoundResultsAdapter(currentClub, toList(resultsObservable)));
  }
}
