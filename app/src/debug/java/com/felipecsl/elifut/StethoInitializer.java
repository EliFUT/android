package com.felipecsl.elifut;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Interceptor;

import java.util.List;

public final class StethoInitializer {
  public static void initialize(Context context) {
    Stetho.initializeWithDefaults(context);
  }

  public static void addInterceptor(List<Interceptor> list) {
    list.add(new StethoInterceptor());
  }
}
