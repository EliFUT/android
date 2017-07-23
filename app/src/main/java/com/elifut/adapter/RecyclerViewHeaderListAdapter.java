package com.elifut.adapter;

import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

abstract class RecyclerViewHeaderListAdapter<R, J, T extends BaseViewHolder<R>, H extends BaseViewHolder<J>>
    extends RecyclerViewListAdapter<R, T>
    implements StickyRecyclerHeadersAdapter<H> {

  protected J headerData;

  protected RecyclerViewHeaderListAdapter(List<R> data, J headerData) {
    super(data);
    this.headerData = headerData;
  }

  protected abstract BaseViewHolder.Factory<H> headerFactory();

  @Override public long getHeaderId(int position) {
    return 0;
  }

  @Override public void onBindHeaderViewHolder(H holder, int position) {
    holder.bind(headerData);
  }

  @Override public H onCreateHeaderViewHolder(ViewGroup parent) {
    return headerFactory().newInstance(parent, 0);
  }

  protected void setHeaderData(J headerData) {
    this.headerData = headerData;
  }
}
