package com.felipecsl.elifut.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.LeagueDetailsActivity;
import com.felipecsl.elifut.activitiy.SimpleTarget;
import com.felipecsl.elifut.adapter.ClubsAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.widget.DividerItemDecoration;
import com.google.common.base.Preconditions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public class LeagueStandingsFragment extends ElifutFragment {
  private final Target leagueLogoTarget = new SimpleTarget() {
    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      Palette.from(bitmap).generate(palette -> {
        LeagueDetailsActivity activity = (LeagueDetailsActivity) getActivity();
        activity.setToolbarColor(
            palette.getDarkVibrantColor(colorPrimary), palette.getLightMutedColor(colorSecondary));
      });
    }
  };

  @Bind(R.id.recycler_clubs) RecyclerView recyclerView;
  @BindColor(R.color.color_primary) int colorPrimary;
  @BindColor(R.color.color_secondary) int colorSecondary;

  private ClubsAdapter adapter;
  private final CompositeSubscription subscription = new CompositeSubscription();

  public static LeagueStandingsFragment newInstance() {
    return new LeagueStandingsFragment();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_league_standings, container, false);
    ButterKnife.bind(this, view);

    LinearLayoutManager layout = new LinearLayoutManager(
        getContext(), LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layout);
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), null));

    League league = Preconditions.checkNotNull(userPreferences.league());
    subscription.add(leaguePreferences
        .clubsObservable()
        .subscribe(newClubs -> {
          if (adapter == null) {
            initAdapter(newClubs);
          } else {
            adapter.setItems(newClubs);
          }
        }));

    Picasso.with(getContext())
        .load(league.image())
        .into(leagueLogoTarget);

    return view;
  }

  private void initAdapter(List<Club> newClubs) {
    adapter = new ClubsAdapter(newClubs, userPreferences.clubPreference().get());
    StickyRecyclerHeadersDecoration decoration = new StickyRecyclerHeadersDecoration(adapter);
    recyclerView.addItemDecoration(decoration);
    adapter.setHasStableIds(true);
    recyclerView.setAdapter(adapter);
    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        super.onChanged();
        decoration.invalidateHeaders();
      }
    });
  }

  @Override public void onDestroy() {
    super.onDestroy();
    subscription.clear();
  }
}
