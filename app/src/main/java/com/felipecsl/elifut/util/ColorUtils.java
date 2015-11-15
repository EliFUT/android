package com.felipecsl.elifut.util;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

public final class ColorUtils {
  private ColorUtils() {
  }

  public static void colorizeHeader(Activity activity, Toolbar toolbar, TabLayout tabs,
      int primaryColor, int secondaryColor) {
    toolbar.setBackgroundColor(primaryColor);
    tabs.setBackgroundColor(primaryColor);
    tabs.setSelectedTabIndicatorColor(secondaryColor);
    activity.getWindow().setStatusBarColor(primaryColor);
  }
}
