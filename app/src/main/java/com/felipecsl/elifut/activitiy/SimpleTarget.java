package com.felipecsl.elifut.activitiy;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public abstract class SimpleTarget implements Target {
  @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
  }

  @Override public void onBitmapFailed(Drawable errorDrawable) {
  }

  @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
  }
}
