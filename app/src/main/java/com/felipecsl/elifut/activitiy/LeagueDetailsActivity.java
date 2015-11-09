package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.felipecsl.elifut.ElifutPreferences;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.ClubsAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.widget.DividerItemDecoration;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import icepick.State;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class LeagueDetailsActivity extends ElifutActivity {
  private static final String EXTRA_LEAGUE = "EXTRA_LEAGUE";
  private static final String TAG = LeagueDetailsActivity.class.getSimpleName();
  private static final String EXTRA_CURRENT_CLUB = "EXTRA_CURRENT_CLUB";
  private final Target leagueLogoTarget = new SimpleTarget() {
    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      Palette.from(bitmap).generate(palette -> {
        int color = palette.getDarkVibrantColor(colorPrimary);
        toolbar.setBackgroundColor(color);
        getWindow().setStatusBarColor(color);
      });
    }
  };

  @Bind(R.id.recycler_clubs) RecyclerView recyclerView;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.progress_bar_layout) ViewGroup progressBarLayout;
  @Bind(R.id.fab) FloatingActionButton nextButton;
  @BindColor(R.color.color_primary) int colorPrimary;

  @Inject ElifutPreferences preferences;

  @State League league;
  @State Club currentClub;
  @State ArrayList<Club> clubs;

  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private final Observer<List<Club>> observer = new SimpleResponseObserver<List<Club>>() {
    @Override public void onError(Throwable e) {
      progressBarLayout.setVisibility(View.GONE);
      Toast.makeText(LeagueDetailsActivity.this, "Failed to load list of clubs",
          Toast.LENGTH_SHORT).show();
      Log.w(TAG, e);
    }

    @Override public void onNext(List<Club> response) {
      clubs = new ArrayList<>(response);
      preferences.storeLeagueClubs(Observable.from(response));
      progressBarLayout.setVisibility(View.GONE);
      getSupportActionBar().setTitle(league.name());
      ClubsAdapter adapter = new ClubsAdapter(response, currentClub);
      recyclerView.setAdapter(adapter);
      recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));

      Subscription subscription = RxView.clicks(nextButton).subscribe((v) -> {
        Club randomClub = response.get(new Random().nextInt(response.size()));
        startActivity(MatchProgressActivity.newIntent(
            LeagueDetailsActivity.this, currentClub, randomClub));
        finish();
      });
      subscriptions.add(subscription);
    }
  };

  public static Intent newIntent(Context context, League league, Club currentClub) {
    return new Intent(context, LeagueDetailsActivity.class)
        .putExtra(EXTRA_LEAGUE, league)
        .putExtra(EXTRA_CURRENT_CLUB, currentClub);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_league_details);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layout);
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(new DividerItemDecoration(this, null));

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      league = intent.getParcelableExtra(EXTRA_LEAGUE);
      currentClub = intent.getParcelableExtra(EXTRA_CURRENT_CLUB);
      Subscription subscription = preferences.retrieveLeagueClubs()
          .switchIfEmpty(clubsObservable())
          .toList()
          .subscribe(observer);
      subscriptions.add(subscription);
    } else {
      observer.onNext(clubs);
    }

    loadLeagueImage();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    subscriptions.clear();
  }

  private void loadLeagueImage() {
    Picasso.with(this)
        .load(league.image())
        .into(leagueLogoTarget);
  }

  private Observable<Club> clubsObservable() {
    return service.clubsByLeague(league.id())
        .compose(this.<List<Club>>applyTransformations())
        .flatMap(Observable::from);
  }
}
