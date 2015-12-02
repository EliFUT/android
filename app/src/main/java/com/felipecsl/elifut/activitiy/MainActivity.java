package com.felipecsl.elifut.activitiy;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.felipecsl.elifut.CompletionObserver;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.ResponseObserver;
import com.felipecsl.elifut.adapter.CountriesSpinnerAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends ElifutActivity {
  private static final String TAG = "MainActivity";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.input_name) EditText inputName;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.countries_spinner) Spinner countriesSpinner;
  @Bind(R.id.loading_frame) FrameLayout loadingFrame;
  @Bind(R.id.fab) FloatingActionButton okButton;

  private Club userClub;
  private CountriesSpinnerAdapter nationsAdapter;
  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private final Observer<List<Nation>> nationObserver =
      new ResponseObserver<List<Nation>>(this, TAG, "Failed to load list of countries.") {
        @Override public void onNext(List<Nation> response) {
          nationsAdapter = new CountriesSpinnerAdapter(MainActivity.this, response);
          countriesSpinner.setAdapter(nationsAdapter);
        }
      };

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    Nation nation = userPreferences.nationPreference().get();

    if (nation != null) {
      launchHomeScreen();
    } else {
      subscriptions.add(service.nations()
          .compose(this.<List<Nation>>applyTransformations())
          .subscribe(nationObserver));
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    subscriptions.unsubscribe();
  }

  @OnClick(R.id.fab) public void onClickNext() {
    loadingFrame.setVisibility(View.VISIBLE);
    okButton.setVisibility(View.GONE);
    Nation nation = (Nation) nationsAdapter.getItem(countriesSpinner.getSelectedItemPosition());
    userPreferences.nationPreference().set(nation);
    userPreferences.coachPreference().set(inputName.getText().toString());

    subscriptions.add(service.randomClub(nation.id())
        .compose(this.<Club>applyTransformations())
        .flatMap(club -> {
          userClub = club;
          userPreferences.clubPreference().set(club);
          return service.league(club.league_id())
              .compose(this.<League>applyTransformations());
        })
        .flatMap(league -> {
          userPreferences.leaguePreference().set(league);
          return service.clubsByLeague(league.id())
              .compose(applyTransformations());
        })
        .flatMap(clubs -> {
          leaguePreferences.putClubsAndInitRounds(Observable.from(clubs));
          return Observable.empty();
        })
        .subscribe(new CompletionObserver<Object>(this, TAG, "Failed to load game data.") {
          @Override public void onCompleted() {
            launchHomeScreen();
          }
        }));
  }

  private void launchHomeScreen() {
    Club club = userPreferences.clubPreference().get();
    startActivity(CurrentTeamDetailsActivity.newIntent(MainActivity.this, club));
    finish();
  }
}
