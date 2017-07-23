package com.elifut.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder
    implements BindableViewHolder<T> {
  BaseViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
    super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
  }

  interface Factory<R extends BaseViewHolder<?>> {
    R newInstance(ViewGroup parent, int viewType);
  }
}