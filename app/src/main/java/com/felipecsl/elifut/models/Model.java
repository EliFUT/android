package com.felipecsl.elifut.models;

import android.os.Parcelable;

public abstract class Model implements Parcelable {
  public abstract int id();
  public abstract int base_id();

  // needed workaround for now.
  @Override public int describeContents() {
    return 0;
  }
}
