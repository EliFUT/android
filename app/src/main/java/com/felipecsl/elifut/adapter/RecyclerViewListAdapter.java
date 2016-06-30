package com.felipecsl.elifut.adapter;

import com.google.common.collect.ImmutableList;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerViewListAdapter<R, T extends BaseViewHolder<R>>
    extends RecyclerView.Adapter<T> {
  protected List<? extends R> data;

  protected RecyclerViewListAdapter(List<R> data) {
    this.data = data;
  }

  protected abstract BaseViewHolder.Factory<T> itemFactory();

  @Override public T onCreateViewHolder(ViewGroup parent, int viewType) {
    return itemFactory().newInstance(parent, viewType);
  }

  @Override public void onBindViewHolder(T holder, int position) {
    holder.bind(data.get(position));
  }

  @Override public long getItemId(int position) {
    return data.get(position).hashCode();
  }

  @Override public int getItemCount() {
    return data.size();
  }

  public void setData(List<? extends R> newData) {
    data = ImmutableList.copyOf(newData);
    notifyDataSetChanged();
  }
}
