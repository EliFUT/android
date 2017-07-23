package com.elifut.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.elifut.R;
import com.elifut.adapter.LargePlayerViewHolder;
import com.elifut.animations.AnimationUtil;
import com.elifut.models.Club;
import com.elifut.models.Player;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Displays a single Player card on the screen, also providing the ability to replace it by
 * selecting a different one (only possible if the user is looking at a player of his own team).
 */
public class PlayerDetailsActivity extends ElifutActivity {
  private static final String EXTRA_PLAYER = "EXTRA_PLAYER";
  private static final String EXTRA_CLUB = "EXTRA_CLUB";
  private static final int REQUEST_REPLACE = 9090;

  @BindView(R.id.layout_root) CoordinatorLayout rootLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindColor(R.color.color_primary) int colorPrimary;

  @State Player player;
  @State Club club;

  public static Intent newIntent(Context context, Player player, Club club) {
    return new Intent(context, PlayerDetailsActivity.class)
        .putExtra(EXTRA_PLAYER, player)
        .putExtra(EXTRA_CLUB, club);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AnimationUtil.setupActivityTransition(this);
    setContentView(R.layout.activity_player_details);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    ActionBar actionBar = checkNotNull(getSupportActionBar());
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      player = intent.getParcelableExtra(EXTRA_PLAYER);
      club = intent.getParcelableExtra(EXTRA_CLUB);
    }

    LargePlayerViewHolder viewHolder = new LargePlayerViewHolder(rootLayout, club);
    viewHolder.bind(player);
    rootLayout.addView(viewHolder.itemView);

    colorizeToolbar(club, toolbar, colorPrimary);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (userPreferences.isCurrentUserClub(club)) {
      getMenuInflater().inflate(R.menu.menu_player_details, menu);
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
    if (itemId == android.R.id.home) {
      ActivityCompat.finishAfterTransition(this);
    } else if (itemId == R.id.action_replace) {
      startActivityForResult(TeamPlayersActivity.newIntent(this, player, club), REQUEST_REPLACE);
    }
    return true;
  }

  @Override protected void onActivityResult(int requestCode, int responseCode, Intent data) {
    super.onActivityResult(requestCode, responseCode, data);
    if (requestCode == REQUEST_REPLACE) {
      finish();
    }
  }
}
