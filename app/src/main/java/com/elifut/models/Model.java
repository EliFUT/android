package com.elifut.models;

import android.os.Parcelable;

public abstract class Model implements Parcelable {
  public abstract int id();
  public abstract int base_id();
}
