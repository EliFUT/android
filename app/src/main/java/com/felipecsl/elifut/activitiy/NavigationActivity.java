package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class NavigationActivity extends ElifutActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  @Inject UserPreferences userPreferences;
  @Inject LeaguePreferences leaguePreferences;

  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Bind(R.id.nav_view) NavigationView navigationView;
  @Bind(R.id.toolbar) Toolbar toolbar;

  private final NavigationHeaderViewHolder headerViewHolder = new NavigationHeaderViewHolder();

  static class NavigationHeaderViewHolder {
    @Bind(R.id.text_coach_name) TextView txtCoachName;
    @Bind(R.id.text_team_name) TextView txtTeamName;
    @Bind(R.id.img_club_logo) ImageView imgClubLogo;
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, LeagueDetailsActivity.class);
  }

  @LayoutRes protected abstract int layoutId();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layoutId());
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    daggerComponent().inject(this);

    Club club = userPreferences.club();

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener((view) -> {
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
    });

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.setDrawerListener(toggle);
    toggle.syncState();

    navigationView.setNavigationItemSelectedListener(this);

    ButterKnife.bind(headerViewHolder, navigationView.getHeaderView(0));

    headerViewHolder.txtCoachName.setText(userPreferences.coachName());
    headerViewHolder.txtTeamName.setText(club.name());

    Picasso.with(this)
        .load(club.large_image())
        .into(headerViewHolder.imgClubLogo);
  }

  @Override public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (item.getItemId()) {
      case android.R.id.home:
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.nav_team) {
      startActivity(TeamDetailsActivity.newIntent(this, userPreferences.club()));
    } else if (id == R.id.nav_league) {
      startActivity(LeagueDetailsActivity.newIntent(this));
    }

    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }
}
