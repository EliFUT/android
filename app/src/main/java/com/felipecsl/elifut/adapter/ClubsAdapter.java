package com.felipecsl.elifut.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.TeamDetailsActivity;
import com.felipecsl.elifut.adapter.ClubsAdapter.HeaderViewHolder;
import com.felipecsl.elifut.adapter.ClubsAdapter.ViewHolder;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubStats;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

import static com.felipecsl.elifut.util.CollectionUtils.sort;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ClubsAdapter
    extends RecyclerViewHeaderListAdapter<Club, Void, ViewHolder, HeaderViewHolder> {
  private final Club selectedClub;

  public ClubsAdapter(Club selectedClub) {
    super(new ArrayList<>(), null);
    this.selectedClub = checkNotNull(selectedClub);
  }

  public void setItems(List<Club> newItems) {
    setData(sort(newItems, (c1, c2) -> c2.nonNullStats().points() - c1.nonNullStats().points()));
  }

  @Override protected BaseViewHolder.Factory<HeaderViewHolder> headerFactory() {
    return (parent, viewType) -> new HeaderViewHolder(parent);
  }

  @Override protected BaseViewHolder.Factory<ViewHolder> itemFactory() {
    return (parent, viewType) -> new ViewHolder(parent);
  }

  class ViewHolderImpl {
    @Bind(R.id.layout) ViewGroup layout;
    @Bind(R.id.txt_position) TextView position;
    @Bind(R.id.txt_club_name) TextView clubName;
    @Bind(R.id.txt_points) TextView points;
    @Bind(R.id.txt_wins) TextView wins;
    @Bind(R.id.txt_draws) TextView draws;
    @Bind(R.id.txt_losses) TextView losses;
    @Bind(R.id.txt_goals_difference) TextView goalsDifference;
  }

  class ViewHolder extends BaseViewHolder<Club> {
    private final ViewHolderImpl views = new ViewHolderImpl();

    ViewHolder(ViewGroup parent) {
      super(parent, R.layout.club_item);
      ButterKnife.bind(views, itemView);
    }

    @Override public void bind(Club club) {
      ClubStats stats = club.nonNullStats();
      int typeface = selectedClub.nameEquals(club) ? Typeface.BOLD : Typeface.NORMAL;
      views.position.setText(String.valueOf(getAdapterPosition() + 1));
      views.points.setTypeface(null, typeface);
      views.points.setText(String.valueOf(stats.points()));
      views.clubName.setText(club.abbrev_name());
      views.clubName.setTypeface(null, typeface);
      views.wins.setTypeface(null, typeface);
      views.wins.setText(String.valueOf(stats.wins()));
      views.draws.setTypeface(null, typeface);
      views.draws.setText(String.valueOf(stats.draws()));
      views.losses.setTypeface(null, typeface);
      views.losses.setText(String.valueOf(stats.losses()));
      views.goalsDifference.setTypeface(null, typeface);
      views.goalsDifference.setText(String.valueOf(stats.goals()));
      Context context = itemView.getContext();
      views.layout.setOnClickListener(view -> context.startActivity(
          TeamDetailsActivity.newIntent(context, club)));
    }
  }

  class HeaderViewHolder extends BaseViewHolder<Void> {
    private final ViewHolderImpl views = new ViewHolderImpl();

    @BindColor(android.R.color.transparent) int transparent;
    @BindColor(R.color.club_table_header_bg) int headerBg;
    @BindColor(R.color.club_table_header_text_color) int headerTextColor;

    HeaderViewHolder(ViewGroup parent) {
      super(parent, R.layout.club_item);
      ButterKnife.bind(views, itemView);
      ButterKnife.bind(this, itemView);
    }

    @Override public void bind(Void unused) {
      views.layout.setBackgroundColor(headerBg);
      views.clubName.setText(R.string.team);
      views.clubName.setTextColor(headerTextColor);
      views.clubName.setBackgroundColor(transparent);
      views.points.setText("P");
      views.points.setTextColor(headerTextColor);
      views.points.setBackgroundColor(transparent);
      views.wins.setText("W");
      views.wins.setTextColor(headerTextColor);
      views.wins.setBackgroundColor(transparent);
      views.draws.setText("D");
      views.draws.setTextColor(headerTextColor);
      views.draws.setBackgroundColor(transparent);
      views.losses.setText("L");
      views.losses.setTextColor(headerTextColor);
      views.losses.setBackgroundColor(transparent);
      views.goalsDifference.setText("G");
      views.goalsDifference.setTextColor(headerTextColor);
      views.goalsDifference.setBackgroundColor(transparent);
    }
  }
}
