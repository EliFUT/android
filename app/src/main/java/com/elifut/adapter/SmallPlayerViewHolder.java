package com.elifut.adapter;

import android.view.ViewGroup;

import com.elifut.R;
import com.elifut.models.Club;

public class SmallPlayerViewHolder extends PlayerViewHolder {
  public SmallPlayerViewHolder(ViewGroup parent, Club club) {
    super(parent, club, R.layout.player_item_small);
  }
}
