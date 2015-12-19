package com.felipecsl.elifut;

import com.google.common.io.Closeables;

import java.io.Closeable;
import java.io.IOException;

public final class Util {
  private Util() {
  }

  public static void closeQuietly(Closeable closeable) {
    try {
      Closeables.close(closeable, true);
    } catch (IOException ignored) {
    }
  }
}
