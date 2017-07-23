package com.elifut.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.elifut.R;
import com.elifut.adapter.ViewPagerAdapter;
import com.elifut.fragment.TeamDetailsFragment;
import com.elifut.fragment.TeamSquadFragment;
import com.elifut.models.Club;
import com.elifut.models.League;
import com.elifut.models.Nation;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;

import static com.elifut.util.ColorUtils.colorizeTabsAndHeader;
import static com.google.common.base.Preconditions.checkNotNull;

public class CurrentTeamDetailsActivity extends NavigationActivity implements TabbedActivity {
  private static final String EXTRA_CLUB = "EXTRA_CLUB";

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.viewpager) ViewPager viewPager;
  @BindView(R.id.tabs) TabLayout tabLayout;

  @State Club club;
  @State String coachName;
  @State Nation nation;
  @State League league;

  public static Intent newIntent(Context context, Club team) {
    return new Intent(context, CurrentTeamDetailsActivity.class)
        .putExtra(EXTRA_CLUB, team);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      club = getIntent().getParcelableExtra(EXTRA_CLUB);
      coachName = userPreferences.coach();
      nation = userPreferences.nation();
      league = userPreferences.league();
    }

    navigationView.setCheckedItem(R.id.nav_team);

    ActionBar actionBar = checkNotNull(getSupportActionBar());
    actionBar.setTitle(club.shortName());
    setupViewPager();
  }

  @Override protected int layoutId() {
    return R.layout.activity_current_team_details;
  }

  private void setupViewPager() {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(TeamDetailsFragment.newInstance(club, coachName, nation, league),
        getString(R.string.infos));
    adapter.addFragment(TeamSquadFragment.newInstance(club), getString(R.string.players));
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
  }

  @Override public void setToolbarColor(int primaryColor, int secondaryColor) {
    colorizeTabsAndHeader(this, toolbar, tabLayout, primaryColor, secondaryColor);
  }
}
