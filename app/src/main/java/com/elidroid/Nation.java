package com.elidroid;

import android.support.annotation.StringRes;

import java.util.Arrays;
import java.util.List;

final class Nation {
  private static final List<Nation> ALL_NATIONS = Arrays.asList(
      new Nation(1, R.string.albania),
      new Nation(27, R.string.italy)
  );
  private final int id;
  private final int nameId;

  Nation(int id, @StringRes int nameId) {
    this.id = id;
    this.nameId = nameId;
  }

  public String image() {
    return null;
  }
}
