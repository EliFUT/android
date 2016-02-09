package com.felipecsl.elifut.fragment;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.PlayerDetailsActivity;
import com.felipecsl.elifut.adapter.SmallPlayerViewHolder;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.services.ClubDataStore;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.util.FragmentBundler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;
import rx.Observer;

public class TeamSquadFragment extends ElifutFragment {
  private static final String EXTRA_CLUB = "EXTRA_CLUB";
  private static final String TAG = "TeamSquadFragment";

  @Inject ElifutDataStore persistenceService;
  @Inject ClubDataStore clubDataStore;

  @State Club club;
  @State ArrayList<Player> players;

  @Bind(R.id.player_gk) FrameLayout gk;
  @Bind(R.id.player_lb) FrameLayout lb;
  @Bind(R.id.player_cb1) FrameLayout cb1;
  @Bind(R.id.player_cb2) FrameLayout cb2;
  @Bind(R.id.player_rb) FrameLayout rb;
  @Bind(R.id.player_cm1) FrameLayout cm1;
  @Bind(R.id.player_cm2) FrameLayout cm2;
  @Bind(R.id.player_cm3) FrameLayout cm3;
  @Bind(R.id.player_cm4) FrameLayout cm4;
  @Bind(R.id.player_at1) FrameLayout at1;
  @Bind(R.id.player_at2) FrameLayout at2;
  @Bind(R.id.player_at3) FrameLayout at3;
  @Bind(R.id.player_at4) FrameLayout at4;

  private boolean hasSavedState;
  private final Observer<ClubSquad> playersObserver = new Observer<ClubSquad>() {
    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
      Log.d(TAG, "Failed to load players", e);
    }

    @Override public void onNext(ClubSquad clubSquad) {
      players = new ArrayList<>(clubSquad.players());
      onPlayersLoaded();
    }
  };

  public static TeamSquadFragment newInstance(Club club) {
    return FragmentBundler.make(new TeamSquadFragment())
        .putParcelable(EXTRA_CLUB, club)
        .build();
  }

  @Nullable @Override public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_team_squad, container, false);

    ButterKnife.bind(this, v);
    daggerComponent().inject(this);

    hasSavedState = savedInstanceState != null;

    return v;
  }

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) {
      if (!hasSavedState) {
        club = getArguments().getParcelable(EXTRA_CLUB);
        loadPlayers();
      } else {
        onPlayersLoaded();
      }
    }
  }

  private void onPlayersLoaded() {
    FluentIterable<Player> defenders = defenders();
    FluentIterable<Player> midfielders = midfielders();
    FluentIterable<Player> attackers = attackers();

    loadPlayer(gk, playerByPosition("GK"), 200);
    loadPlayer(lb, defenders.get(0), 220);
    loadPlayer(cb1, defenders.get(1), 230);
    loadPlayer(cb2, defenders.get(2), 240);
    loadPlayer(rb, defenders.get(3), 250);
    loadPlayer(cm1, midfielders.get(0), 270);
    loadPlayer(cm2, midfielders.get(1), 280);
    loadPlayer(cm3, midfielders.get(2), 290);
    loadPlayer(cm4, midfielders.get(3), 300);
    loadPlayer(at2, attackers.first().get(), 320);
    loadPlayer(at3, attackers.last().get(), 330);
  }

  private FluentIterable<Player> defenders() {
    return findByAnyPosition(Player.DEFENDER_POSITIONS);
  }

  private FluentIterable<Player> attackers() {
    return findByAnyPosition(Player.ATTACKER_POSITIONS);
  }

  private FluentIterable<Player> midfielders() {
    return findByAnyPosition(Player.MIDFIELDER_POSITIONS);
  }

  private FluentIterable<Player> findByAnyPosition(List<String> possibilities) {
    return findPlayerBy(p -> possibilities.indexOf(p.position()) != -1);
  }

  private FluentIterable<Player> playersByPosition(String position) {
    return findPlayerBy(p -> p.position().equals(position));
  }

  @Nullable private Player playerByPosition(String position) {
    return playersByPosition(position).first().get();
  }

  private FluentIterable<Player> findPlayerBy(Predicate<Player> predicate) {
    return FluentIterable.from(players).filter(predicate);
  }

  private void loadPlayer(ViewGroup target, Player player, long delay) {
    SelectableSmallPlayerViewHolder viewHolder = new SelectableSmallPlayerViewHolder(target, club);
    viewHolder.bind(player);
    target.removeAllViews();
    viewHolder.itemView.setAlpha(0);
    viewHolder.itemView.setScaleX(0.8f);
    viewHolder.itemView.setScaleY(0.8f);
    viewHolder.itemView.animate()
        .scaleX(1)
        .scaleY(1)
        .setStartDelay(delay)
        .alpha(1);
    target.addView(viewHolder.itemView);
  }

  private void loadPlayers() {
    clubDataStore.squadObservable(club).subscribe(playersObserver);
  }

  class SelectableSmallPlayerViewHolder extends SmallPlayerViewHolder {
    SelectableSmallPlayerViewHolder(ViewGroup parent, Club club) {
      super(parent, club);
    }

    @Override protected void onClickPlayer(View view, Player player) {
      super.onClickPlayer(view, player);
      Context context = itemView.getContext();
      //noinspection unchecked
      ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
          (Activity) context,
          Pair.create(imgPlayer, "player_image"),
          Pair.create(imgClub, "img_player_club"),
          Pair.create(imgNation, "img_player_nation"),
          Pair.create(imgPlayerQuality, "img_player_quality"));
      Intent intent = PlayerDetailsActivity.newIntent(context, player, club);
      context.startActivity(intent, options.toBundle());
    }
  }
}
