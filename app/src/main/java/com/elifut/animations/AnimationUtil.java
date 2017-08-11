package com.elifut.animations;

import android.app.Activity;
import android.view.Window;

import com.elifut.transitions.TransitionUtil;

public final class AnimationUtil {
  private AnimationUtil() {
  }

  public static void setupActivityTransition(Activity activity) {
    Window window = activity.getWindow();
    window.setAllowEnterTransitionOverlap(true);
    window.setEnterTransition(TransitionUtil.makeFadeTransition());
  }
}
