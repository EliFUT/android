package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.felipecsl.elifut.AnimationUtil;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.LargePlayerViewHolder;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.util.ColorUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import icepick.State;

import static com.google.common.base.Preconditions.checkNotNull;

public class PlayerDetailsActivity extends ElifutActivity {
  private static final String EXTRA_PLAYER = "EXTRA_PLAYER";
  private static final String EXTRA_CLUB = "EXTRA_CLUB";

  @Bind(R.id.layout_root) CoordinatorLayout rootLayout;
  @Bind(R.id.toolbar) Toolbar toolbar;
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

    Picasso.with(this)
        .load(club.large_image())
        .into(new SimpleTarget() {
          @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Palette.from(bitmap).generate(palette ->
                ColorUtils.colorizeHeader(PlayerDetailsActivity.this, toolbar,
                    palette.getDarkVibrantColor(colorPrimary))
            );
          }
        });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      ActivityCompat.finishAfterTransition(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
