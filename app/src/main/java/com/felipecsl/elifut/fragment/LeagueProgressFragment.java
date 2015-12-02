package com.felipecsl.elifut.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.LeagueNextMatchesAdapter;
import com.felipecsl.elifut.models.LeagueRound;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public class LeagueProgressFragment extends ElifutFragment {
  @Bind(R.id.recycler_next_matches) RecyclerView recyclerView;

  private final CompositeSubscription subscription = new CompositeSubscription();
  private LeagueNextMatchesAdapter adapter;

  public static LeagueProgressFragment newInstance() {
    return new LeagueProgressFragment();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_league_progress, container, false);
    ButterKnife.bind(this, view);

    LinearLayoutManager layout = new LinearLayoutManager(
        getActivity(), LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layout);
    recyclerView.setHasFixedSize(true);

    subscription.add(leaguePreferences
        .roundsObservable()
        .subscribe(rounds -> {
          // TODO: This is gonna blow up if there are no rounds left
          LeagueRound round = rounds.get(0);
          if (adapter == null) {
            initAdapter(round);
          } else {
            adapter.setItems(round);
          }
        }));

    return view;
  }

  private void initAdapter(LeagueRound round) {
    adapter = new LeagueNextMatchesAdapter(round);
    adapter.setHasStableIds(true);
    StickyRecyclerHeadersDecoration decoration = new StickyRecyclerHeadersDecoration(adapter);
    recyclerView.addItemDecoration(decoration);
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
