package com.felipecsl.elifut;

import android.content.Context;

final class MultiDex {
  /** Install multidex in debug application. */
  static void installMultiDex(Context base) {
    android.support.multidex.MultiDex.install(base);
  }

  private MultiDex() {
  }
}
