package com.elifut.util;

import android.os.Build;

public final class AndroidVersion {
  public static boolean isAtLeastLollipop() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }
}
