package com.elifut.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

public final class ColorUtils {
  private ColorUtils() {
  }

  public static void colorizeTabsAndHeader(Activity activity, Toolbar toolbar, TabLayout tabs,
      int primaryColor, int secondaryColor) {
    tabs.setBackgroundColor(primaryColor);
    tabs.setSelectedTabIndicatorColor(secondaryColor);
    colorizeHeader(activity, toolbar, primaryColor);
  }

  public static void colorizeHeader(Activity activity, Toolbar toolbar, int primaryColor) {
    toolbar.setBackgroundColor(primaryColor);
    activity.getWindow().setStatusBarColor(darken(primaryColor, 0.9f));
  }

  /** Returns darker version of specified color */
  private static int darken(int color, float factor) {
    int a = Color.alpha(color);
    int r = Color.red(color);
    int g = Color.green(color);
    int b = Color.blue(color);

    return Color.argb(a,
        Math.max((int) (r * factor), 0),
        Math.max((int) (g * factor), 0),
        Math.max((int) (b * factor), 0));
  }
}
