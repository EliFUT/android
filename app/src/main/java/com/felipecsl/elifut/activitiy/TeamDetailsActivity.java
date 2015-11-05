package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.felipecsl.elifut.ElifutPreferences;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.ViewPagerAdapter;
import com.felipecsl.elifut.fragment.TeamDetailsFragment;
import com.felipecsl.elifut.fragment.TeamPlayersFragment;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;

public class TeamDetailsActivity extends ElifutActivity {
  private static final String EXTRA_COUNTRY = "EXTRA_COUNTRY";
  private static final String EXTRA_NAME = "EXTRA_NAME";
  private static final String TAG = TeamDetailsActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.viewpager) ViewPager viewPager;
  @Bind(R.id.tabs) TabLayout tabLayout;
  @Bind(R.id.fab) FloatingActionButton fab;

  @Inject ElifutPreferences preferences;

  @State Nation nation;
  @State String coachName;
  @State Club club;
  @State League league;

  public static Intent newIntent(Context context, Nation nation, String name) {
    return new Intent(context, TeamDetailsActivity.class)
        .putExtra(EXTRA_COUNTRY, nation)
        .putExtra(EXTRA_NAME, name);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team_details);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    league = preferences.retrieveUserLeague();
    club = preferences.retrieveUserClub();

    if (league != null && club != null) {
      goNext();
      return;
    }

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      coachName = intent.getStringExtra(EXTRA_NAME);
      nation = intent.getParcelableExtra(EXTRA_COUNTRY);
    }

    if (savedInstanceState == null) {
      loadClub();
    } else {
      loadLeague();
    }
  }

  private void setupViewPager() {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(TeamDetailsFragment.newInstance(club, coachName, nation, league),
        getString(R.string.infos));
    adapter.addFragment(TeamPlayersFragment.newInstance(club), getString(R.string.players));
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
  }

  private void loadClub() {
    service.randomClub(nation.id())
        .compose(this.<Club>applyTransformations())
        .subscribe(new SimpleResponseObserver<Club>() {
          @Override public void onError(Throwable e) {
            Toast.makeText(
                TeamDetailsActivity.this, "Failed to load club", Toast.LENGTH_SHORT).show();
            Log.w(TAG, e);
          }

          @Override public void onNext(Club response) {
            club = response;
            loadLeague();
          }
        });
  }

  private void loadLeague() {
    service.league(club.league_id())
        .compose(this.<League>applyTransformations())
        .subscribe(new SimpleResponseObserver<League>() {
          @Override public void onError(Throwable e) {
            Toast.makeText(TeamDetailsActivity.this, "Failed to load league infos",
                Toast.LENGTH_SHORT).show();
            Log.w(TAG, e);
          }

          @Override public void onNext(League response) {
            league = response;
            getSupportActionBar().setTitle(club.shortName());
            setupViewPager();
            fab.setVisibility(View.VISIBLE);
          }
        });
  }

  public void setToolbarColor(int primaryColor, int secondaryColor) {
    toolbar.setBackgroundColor(primaryColor);
    tabLayout.setBackgroundColor(primaryColor);
    tabLayout.setSelectedTabIndicatorColor(secondaryColor);
    getWindow().setStatusBarColor(primaryColor);
  }

  @OnClick(R.id.fab) public void onClickNext() {
    preferences.storeUserClub(club);
    preferences.storeUserLeague(league);
    goNext();
  }

  private void goNext() {
    startActivity(LeagueDetailsActivity.newIntent(this, league, club));
    finish();
  }
}
