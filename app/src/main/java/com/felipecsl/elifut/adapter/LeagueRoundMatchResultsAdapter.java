package com.felipecsl.elifut.adapter;

import android.view.ViewGroup;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.LeagueRoundMatchResultsAdapter.ItemViewHolder;
import com.felipecsl.elifut.models.MatchResult;

import java.util.List;

import butterknife.ButterKnife;

public final class LeagueRoundMatchResultsAdapter extends
    RecyclerViewHeaderListAdapter<MatchResult, String, ItemViewHolder, SimpleHeaderViewHolder> {

  public LeagueRoundMatchResultsAdapter(List<MatchResult> data, String headerData) {
    super(data, headerData);
  }

  @Override protected BaseViewHolder.Factory<SimpleHeaderViewHolder> headerFactory() {
    return (parent, viewType) ->
        new SimpleHeaderViewHolder(parent, R.layout.adapter_round_header_item);
  }

  @Override protected BaseViewHolder.Factory<ItemViewHolder> itemFactory() {
    return (parent, viewType) -> new ItemViewHolder(parent);
  }

  static class ItemViewHolder extends BaseViewHolder<MatchResult> {
    ItemViewHolder(ViewGroup parent) {
      super(parent, R.layout.adapter_match_result_item);
      ButterKnife.bind(this, itemView);
    }

    @Override public void bind(MatchResult matchResult) {
    }
  }
}
