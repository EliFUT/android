package com.felipecsl.elifut;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.lang.Override;import java.lang.Runnable;import java.util.concurrent.Executor;

public final class ConcurrentUtil {
  private ConcurrentUtil() {
  }

  public static class MainThreadExecutor implements Executor {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void execute(@NonNull Runnable runnable) {
      handler.post(runnable);
    }
  }
}
