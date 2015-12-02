package com.felipecsl.elifut.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.TeamDetailsActivity;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public final class LeagueNextMatchesAdapter
    extends RecyclerView.Adapter<LeagueNextMatchesAdapter.BaseViewHolder<Match>>
    implements StickyRecyclerHeadersAdapter<LeagueNextMatchesAdapter.BaseViewHolder<LeagueRound>> {
  private final List<Match> nextOpponents;
  private final LeagueRound round;

  public LeagueNextMatchesAdapter(LeagueRound round) {
    this.round = round;
    this.nextOpponents = checkNotNull(round).matches();
  }

  @Override public BaseViewHolder<Match> onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(parent);
  }

  @Override public void onBindViewHolder(BaseViewHolder<Match> holder, int position) {
    holder.bind(nextOpponents.get(position));
  }

  @Override public long getHeaderId(int position) {
    return 0;
  }

  @Override public BaseViewHolder<LeagueRound> onCreateHeaderViewHolder(ViewGroup parent) {
    return new HeaderViewHolder(parent);
  }

  @Override public void onBindHeaderViewHolder(BaseViewHolder<LeagueRound> holder, int position) {
    holder.bind(round);
  }

  @Override public int getItemCount() {
    return nextOpponents.size();
  }

  public void setItems(LeagueRound round) {
    nextOpponents.clear();
    nextOpponents.addAll(round.matches());
    notifyDataSetChanged();
  }

  static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder
      implements BindableViewHolder<T> {
    BaseViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
      super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }
  }

  static class ViewHolder extends BaseViewHolder<Match> {
    @Bind(R.id.img_team_home) ImageView imgTeamHome;
    @Bind(R.id.img_team_away) ImageView imgTeamAway;
    @Bind(R.id.txt_team_home) TextView txtTeamHome;
    @Bind(R.id.txt_team_away) TextView txtTeamAway;

    ViewHolder(ViewGroup parent) {
      super(parent, R.layout.adapter_future_match_item);
      ButterKnife.bind(this, itemView);
    }

    @Override public void bind(Match match) {
      Club home = match.home();
      Club away = match.away();

      Picasso.with(itemView.getContext())
          .load(home.large_image())
          .into(imgTeamHome);

      Picasso.with(itemView.getContext())
          .load(away.large_image())
          .into(imgTeamAway);

      txtTeamHome.setText(home.tinyName().toUpperCase());
      txtTeamAway.setText(away.tinyName().toUpperCase());
      Context context = itemView.getContext();
      imgTeamAway.setOnClickListener(view -> context.startActivity(
          TeamDetailsActivity.newIntent(context, away)));
      imgTeamHome.setOnClickListener(view -> context.startActivity(
          TeamDetailsActivity.newIntent(context, home)));
    }
  }

  public static class HeaderViewHolder extends BaseViewHolder<LeagueRound> {
    @Bind(R.id.txt_header) TextView txtHeader;

    HeaderViewHolder(ViewGroup parent) {
      super(parent, R.layout.adapter_round_header);
      ButterKnife.bind(this, itemView);
    }

    @Override public void bind(LeagueRound round) {
      txtHeader.setText(itemView.getContext().getString(R.string.round_n, round.roundNumber()));
    }
  }
}
