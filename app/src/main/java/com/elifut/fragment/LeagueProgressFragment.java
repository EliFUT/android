package com.elifut.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elifut.R;
import com.elifut.adapter.LeagueMatchesAdapter;
import com.elifut.models.Club;
import com.elifut.models.LeagueRound;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class LeagueProgressFragment extends ElifutFragment {
  private static final String TAG = "LeagueProgressFragment";
  @BindView(R.id.recycler_next_matches) RecyclerView recyclerView;

  private final CompositeSubscription subscription = new CompositeSubscription();
  private LeagueMatchesAdapter adapter;

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
    initAdapter();

    subscription.add(leagueDetails
        .roundsObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(rounds -> {
          if (!rounds.isEmpty()) {
            LeagueRound round = rounds.get(0);
            int roundsLeft = leagueDetails.rounds().size();
            String title = getActivity().getString(
                R.string.next_round_n_of_n, round.roundNumber(), roundsLeft + round.roundNumber());
            adapter.setRound(round, title);
          } else {
            // TODO: Display a message on screen indicating the league has ended.
            Log.w(TAG, "League has already ended. Nothing to do here.");
          }
        }));

    return view;
  }

  private void initAdapter() {
    Club club = userPreferences.club();
    adapter = new LeagueMatchesAdapter(club);
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
