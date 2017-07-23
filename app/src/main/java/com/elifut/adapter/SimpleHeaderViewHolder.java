package com.elifut.adapter;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elifut.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class SimpleHeaderViewHolder extends BaseViewHolder<String> {
  @BindView(R.id.txt_header) TextView txtHeader;

  SimpleHeaderViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
    super(parent, layoutId);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bind(String text) {
    txtHeader.setText(text);
  }
}