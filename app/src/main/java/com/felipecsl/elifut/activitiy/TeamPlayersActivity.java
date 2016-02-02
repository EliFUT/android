package com.felipecsl.elifut.activitiy;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.PlayersAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.services.ElifutDataStore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import icepick.State;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An Activity that displays all the Players belonging to the provided club in a RecyclerView. It
 * allows selecting one of the players and upon click, it will finish and return the selected
 * player.
 */
public class TeamPlayersActivity extends ElifutActivity implements PlayersAdapter.Callbacks {
  private static final String EXTRA_CLUB = "EXTRA_CLUB";
  private static final String EXTRA_PREVIOUS_PLAYER = "EXTRA_PREVIOUS_PLAYER";

  @Bind(R.id.recycler_players) RecyclerView playersList;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @BindColor(R.color.color_primary) int colorPrimary;

  @Inject ElifutDataStore persistenceService;

  @State Club club;
  @State Player previousPlayer;
  @State ArrayList<Player> players;

  public static Intent newIntent(Context context, Player previousPlayer, Club club) {
    return new Intent(context, TeamPlayersActivity.class)
        .putExtra(EXTRA_CLUB, club)
        .putExtra(EXTRA_PREVIOUS_PLAYER, previousPlayer);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.team_players_activity);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);
    ActionBar actionBar = checkNotNull(getSupportActionBar());
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);

    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    playersList.setLayoutManager(layoutManager);
    playersList.setHasFixedSize(true);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      club = intent.getParcelableExtra(EXTRA_CLUB);
      previousPlayer = intent.getParcelableExtra(EXTRA_PREVIOUS_PLAYER);
      loadPlayers();
    } else {
      onPlayersLoaded();
    }

    colorizeToolbar(club, toolbar, colorPrimary);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
    if (itemId == android.R.id.home) {
      finish();
    }
    return true;
  }

  private void onPlayersLoaded() {
    PlayersAdapter adapter = new PlayersAdapter(players, club, this);
    playersList.setAdapter(adapter);
  }

  private void loadPlayers() {
    this.players = new ArrayList<>(club.substitutes(persistenceService));
    onPlayersLoaded();
  }

  @Override public void onPlayerSelected(Player player) {
    List<? extends ClubSquad> clubSquads = persistenceService.query(
        AutoValueClasses.CLUB_SQUAD, "club_id = ?", String.valueOf(club.id()));
    ClubSquad clubSquad = clubSquads.get(
        Preconditions.checkElementIndex(0, clubSquads.size(), "Club squad not found"));
    List<Player> squad = new ArrayList<>(clubSquad.squad());
    squad.remove(previousPlayer);
    squad.add(player);
    persistenceService.update(clubSquad.toBuilder().squad(squad).build(), clubSquad.id());
    finish();

    String message = getString(R.string.player_replaced, previousPlayer.name(), player.name());
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }
}
