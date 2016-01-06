package com.felipecsl.elifut;

import android.content.Context;

import com.facebook.stetho.Stetho;

import java.util.List;

import okhttp3.Interceptor;

public final class StethoInitializer {
  public static void initialize(Context context) {
    Stetho.initializeWithDefaults(context);
  }

  public static void addInterceptor(List<Interceptor> list) {
    // TODO: Uncomment when Stetho supports OkHttp 3 and add to the provider in NetworkModule.
//    list.add(new StethoInterceptor());
  }
}
