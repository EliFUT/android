package com.elifut.adapter;

import android.view.ViewGroup;

import com.elifut.R;
import com.elifut.models.Club;

public class LargePlayerViewHolder extends PlayerViewHolder {
  public LargePlayerViewHolder(ViewGroup parent, Club club) {
    super(parent, club, R.layout.player_item_large);
  }
}
