package com.felipecsl.elifut.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public final class LeagueNextMatchesAdapter extends RecyclerView.Adapter<LeagueNextMatchesAdapter.ViewHolder> {
  private final List<Club> nextOpponents;
  private final Club currentClub;

  public LeagueNextMatchesAdapter(List<Club> nextOpponents, Club currentClub) {
    this.nextOpponents = nextOpponents;
    this.currentClub = currentClub;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(parent);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    if (position % 2 == 0) {
      holder.bind(currentClub, nextOpponents.get(position));
    } else {
      holder.bind(nextOpponents.get(position), currentClub);
    }
  }

  @Override public int getItemCount() {
    return nextOpponents.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.img_team_home) ImageView imgTeamHome;
    @Bind(R.id.img_team_away) ImageView imgTeamAway;
    @Bind(R.id.txt_team_home) TextView txtTeamHome;
    @Bind(R.id.txt_team_away) TextView txtTeamAway;

    ViewHolder(ViewGroup parent) {
      super(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.adapter_future_match_item, parent, false));
      ButterKnife.bind(this, itemView);
    }

    void bind(Club home, Club away) {
      Picasso.with(itemView.getContext())
          .load(home.large_image())
          .into(imgTeamHome);

      Picasso.with(itemView.getContext())
          .load(away.large_image())
          .into(imgTeamAway);

      txtTeamHome.setText(home.abbrev_name().substring(0, 3).toUpperCase());
      txtTeamAway.setText(away.abbrev_name().substring(0, 3).toUpperCase());
    }
  }
}
