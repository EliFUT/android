package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.ViewPagerAdapter;
import com.felipecsl.elifut.fragment.TeamDetailsFragment;
import com.felipecsl.elifut.fragment.TeamPlayersFragment;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;

import static com.felipecsl.elifut.util.ColorUtils.colorizeTabsAndHeader;

public class CurrentTeamDetailsActivity extends NavigationActivity implements TabbedActivity {
  private static final String EXTRA_CLUB = "EXTRA_CLUB";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.viewpager) ViewPager viewPager;
  @Bind(R.id.tabs) TabLayout tabLayout;

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

    getSupportActionBar().setTitle(club.shortName());
    setupViewPager();
  }

  @Override protected int layoutId() {
    return R.layout.activity_current_team_details;
  }

  private void setupViewPager() {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(TeamDetailsFragment.newInstance(club, coachName, nation, league),
        getString(R.string.infos));
    adapter.addFragment(TeamPlayersFragment.newInstance(club), getString(R.string.players));
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
  }

  @Override public void setToolbarColor(int primaryColor, int secondaryColor) {
    colorizeTabsAndHeader(this, toolbar, tabLayout, primaryColor, secondaryColor);
  }
}
