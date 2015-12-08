package com.felipecsl.elifut.adapter;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felipecsl.elifut.R;

import butterknife.Bind;
import butterknife.ButterKnife;

class SimpleHeaderViewHolder extends BaseViewHolder<String> {
  @Bind(R.id.txt_header) TextView txtHeader;

  SimpleHeaderViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
    super(parent, layoutId);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bind(String text) {
    txtHeader.setText(text);
  }
}