package com.felipecsl.elifut.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class NationTextView extends AppCompatTextView implements Target {
  public NationTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public NationTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
    setCompoundDrawablesWithIntrinsicBounds(
        new BitmapDrawable(getContext().getResources(), bitmap), null, null, null);
  }

  @Override public void onBitmapFailed(Drawable errorDrawable) {
  }

  @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
  }
}
