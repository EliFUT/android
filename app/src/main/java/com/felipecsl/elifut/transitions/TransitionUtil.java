package com.felipecsl.elifut.transitions;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Fade;
import android.transition.Transition;

public final class TransitionUtil {
  /**
   * Returns a modified enter transition that excludes the navigation bar and status
   * bar as targets during the animation. This ensures that the navigation bar and
   * status bar won't appear to "blink" as they fade in/out during the transition.
   */
  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static Transition makeFadeTransition() {
    return new Fade()
      .excludeTarget(android.R.id.navigationBarBackground, true)
      .excludeTarget(android.R.id.statusBarBackground, true);
  }
}
