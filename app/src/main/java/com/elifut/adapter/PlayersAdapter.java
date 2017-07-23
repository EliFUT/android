package com.elifut.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.elifut.models.Club;
import com.elifut.models.Player;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/** A RecyclerView adapter that displays a list of players and allows singleselection. */
public class PlayersAdapter extends RecyclerViewListAdapter<Player, PlayerViewHolder> {
  private final Club club;
  private final Callbacks callback;

  public PlayersAdapter(List<Player> players, Club club, Callbacks callback) {
    super(checkNotNull(players));
    this.callback = callback;
    this.club = checkNotNull(club);
  }

  @Override protected BaseViewHolder.Factory<PlayerViewHolder> itemFactory() {
    return (parent, viewType) -> new PlayerSelectionViewHolder(parent, club);
  }

  class PlayerSelectionViewHolder extends PlayerViewHolder {
    protected PlayerSelectionViewHolder(ViewGroup parent, Club club) {
      super(parent, club);
    }

    @Override protected void onClickPlayer(View view, Player player) {
      super.onClickPlayer(view, player);
      if (callback != null) {
        callback.onPlayerSelected(player);
      }
    }
  }

  public interface Callbacks {
    void onPlayerSelected(Player player);
  }
}
