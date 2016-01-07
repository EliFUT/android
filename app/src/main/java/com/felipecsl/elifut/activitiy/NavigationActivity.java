package com.felipecsl.elifut.activitiy;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.Util;
import com.felipecsl.elifut.animations.SimpleAnimatorListener;
import com.felipecsl.elifut.match.LeagueRoundExecutor;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.preferences.LeagueDetails;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class NavigationActivity extends ElifutActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private final Handler handler = new Handler();

  @Inject UserPreferences userPreferences;
  @Inject LeagueDetails leagueDetails;
  @Inject LeagueRoundExecutor roundExecutor;

  @Bind(R.id.circular_reveal_overlay) View circularRevealOverlay;
  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Bind(R.id.nav_view) NavigationView navigationView;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.fab) FloatingActionButton fab;
  @BindDimen(R.dimen.fab_margin) int fabMargin;

  private final SimpleTarget clubLogoTarget = new SimpleTarget() {
    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      headerViewHolder.imgClubLogo.setImageBitmap(bitmap);
      Palette.from(bitmap).generate(palette -> {
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
          @Override public Shader resize(int width, int height) {
            return new LinearGradient(0, 0, width, height, new int[] {
                palette.getDarkVibrantColor(0xFF81C784),
                palette.getLightVibrantColor(0xFF2E7D32)
            }, null, Shader.TileMode.CLAMP);
          }
        };
        PaintDrawable paintDrawable = new PaintDrawable();
        paintDrawable.setShape(new RectShape());
        paintDrawable.setShaderFactory(shaderFactory);
        LayerDrawable background = new LayerDrawable(new Drawable[] { paintDrawable });
        //noinspection deprecation
        headerViewHolder.navHeaderLayout.setBackgroundDrawable(background);
      });
    }
  };

  private final NavigationHeaderViewHolder headerViewHolder = new NavigationHeaderViewHolder();

  static class NavigationHeaderViewHolder {
    @Bind(R.id.text_coach_name) TextView txtCoachName;
    @Bind(R.id.text_team_name) TextView txtTeamName;
    @Bind(R.id.img_club_logo) ImageView imgClubLogo;
    @Bind(R.id.nav_header_layout) LinearLayout navHeaderLayout;
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

    Club club = checkNotNull(userPreferences.clubPreference().get());

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.setDrawerListener(toggle);
    toggle.syncState();

    navigationView.setNavigationItemSelectedListener(this);

    ButterKnife.bind(headerViewHolder, navigationView.getHeaderView(0));

    headerViewHolder.txtCoachName.setText(userPreferences.coach());
    headerViewHolder.txtTeamName.setText(club.name());

    Picasso.with(this)
        .load(club.large_image())
        .into(clubLogoTarget);
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

    switch (id) {
      case android.R.id.home:
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.nav_team) {
      Club club = userPreferences.club();
      startActivity(CurrentTeamDetailsActivity.newIntent(this, club));
      finish();
    } else if (id == R.id.nav_league) {
      startActivity(LeagueDetailsActivity.newIntent(this));
      finish();
    }

    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  @OnClick(R.id.fab) public void onClickFab() {
    LeagueRound round = leagueDetails.nextRound();
    circularRevealOverlay.setVisibility(View.VISIBLE);
    int distFromEdge = fabMargin + (fab.getWidth() / 2);
    int cx = drawerLayout.getWidth() - distFromEdge;
    int cy = drawerLayout.getHeight() - distFromEdge;
    float finalRadius = Math.max(drawerLayout.getWidth(), drawerLayout.getHeight());
    Animator circularReveal = ViewAnimationUtils
        .createCircularReveal(circularRevealOverlay, cx, cy, 0, finalRadius)
        .setDuration(400);
    circularReveal.addListener(new SimpleAnimatorListener() {
      @Override public void onAnimationEnd(Animator animation) {
        startActivity(MatchProgressActivity.newIntent(NavigationActivity.this, round));
        circularRevealOverlay.postDelayed(() ->
            circularRevealOverlay.setVisibility(View.GONE), 1000);
        Util.defer(() -> roundExecutor.execute(round.matches()));
      }
    });
    circularReveal.start();
  }
}
