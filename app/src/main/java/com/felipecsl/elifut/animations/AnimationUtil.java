package com.felipecsl.elifut.animations;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;

import com.felipecsl.elifut.transitions.TransitionUtil;
import com.felipecsl.elifut.util.AndroidVersion;

public final class AnimationUtil {
  private AnimationUtil() {
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void setupActivityTransition(Activity activity) {
    if (AndroidVersion.isAtLeastLollipop()) {
      Window window = activity.getWindow();
      window.setAllowEnterTransitionOverlap(true);
      window.setEnterTransition(TransitionUtil.makeFadeTransition());
      window.setExitTransition(TransitionUtil.makeFadeTransition());
    }
  }
}
