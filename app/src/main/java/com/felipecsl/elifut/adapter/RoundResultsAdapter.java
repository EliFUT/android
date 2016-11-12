package com.felipecsl.elifut.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activity.TeamDetailsActivity;
import com.felipecsl.elifut.adapter.RoundResultsAdapter.ViewHolder;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public final class RoundResultsAdapter extends RecyclerViewListAdapter<Match, ViewHolder> {
  private final Club currentClub;

  public RoundResultsAdapter(Club currentClub, List<Match> matches) {
    super(matches);
    this.currentClub = currentClub;
  }

  @Override protected BaseViewHolder.Factory<ViewHolder> itemFactory() {
    return (parent, viewType) -> new ViewHolder(parent);
  }

  class ViewHolder extends BaseViewHolder<Match> {
    @BindView(R.id.outer_layout) FrameLayout layoutOuter;
    @BindView(R.id.layout_team_home) LinearLayout layoutTeamHome;
    @BindView(R.id.layout_team_away) LinearLayout layoutTeamAway;
    @BindView(R.id.img_team_home) ImageView imgTeamHome;
    @BindView(R.id.img_team_away) ImageView imgTeamAway;
    @BindView(R.id.txt_team_home) TextView txtTeamHome;
    @BindView(R.id.txt_team_away) TextView txtTeamAway;
    @BindView(R.id.txt_team_home_score) TextView txtTeamHomeScore;
    @BindView(R.id.txt_team_away_score) TextView txtTeamAwayScore;

    @BindColor(R.color.light_gray) int colorLightGray;
    @BindColor(android.R.color.transparent) int colorTransparent;
    @BindColor(R.color.material_red_300) int colorCurrentTeam;

    ViewHolder(ViewGroup parent) {
      super(parent, R.layout.adapter_match_result_item);
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
      layoutTeamAway.setOnClickListener(view -> context.startActivity(
          TeamDetailsActivity.newIntent(context, away)));
      layoutTeamHome.setOnClickListener(view -> context.startActivity(
          TeamDetailsActivity.newIntent(context, home)));

      txtTeamHomeScore.setText(String.valueOf(match.result().homeGoals().size()));
      txtTeamAwayScore.setText(String.valueOf(match.result().awayGoals().size()));

      if (!match.hasClub(currentClub)) {
        layoutOuter.setBackgroundColor(
            getAdapterPosition() % 2 != 0 ? colorLightGray : colorTransparent);
      } else {
        layoutOuter.setBackgroundColor(colorCurrentTeam);
      }
    }
  }
}
