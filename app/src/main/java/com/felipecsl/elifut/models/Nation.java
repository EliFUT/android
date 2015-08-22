package com.felipecsl.elifut.models;

import android.support.annotation.StringRes;

final class Nation {
  private final int id;
  private final int nameId;

  Nation(int id, @StringRes int nameId) {
    this.id = id;
    this.nameId = nameId;
  }

  public String image() {
    return null;
  }

  public static Nation fromId(int id) {
    return null;
  }
}
