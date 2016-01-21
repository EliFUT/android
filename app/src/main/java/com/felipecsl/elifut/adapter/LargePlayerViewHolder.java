package com.felipecsl.elifut.adapter;

import android.view.ViewGroup;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;

public class LargePlayerViewHolder extends PlayerViewHolder {
  public LargePlayerViewHolder(ViewGroup parent, Club club) {
    super(parent, club, R.layout.player_item_large);
  }
}
