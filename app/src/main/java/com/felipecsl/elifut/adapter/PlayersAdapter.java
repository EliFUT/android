package com.felipecsl.elifut.adapter;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Player;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class PlayersAdapter extends RecyclerViewListAdapter<Player, PlayerViewHolder> {
  private final Club club;

  public PlayersAdapter(List<Player> players, Club club) {
    super(checkNotNull(players));
    this.club = checkNotNull(club);
  }

  @Override protected BaseViewHolder.Factory<PlayerViewHolder> itemFactory() {
    return (parent, viewType) -> new PlayerViewHolder(parent, club);
  }
}
