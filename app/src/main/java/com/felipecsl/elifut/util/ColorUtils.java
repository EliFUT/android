package com.felipecsl.elifut.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

public final class ColorUtils {
  private ColorUtils() {
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void colorizeHeader(Activity activity, Toolbar toolbar, TabLayout tabs,
      int primaryColor, int secondaryColor) {
    toolbar.setBackgroundColor(primaryColor);
    tabs.setBackgroundColor(primaryColor);
    tabs.setSelectedTabIndicatorColor(secondaryColor);
    if (AndroidVersion.isAtLeastLollipop()) {
      activity.getWindow().setStatusBarColor(primaryColor);
    }
  }
}
