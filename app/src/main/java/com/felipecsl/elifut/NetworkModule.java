package com.felipecsl.elifut;

import android.content.Context;

import com.felipecsl.elifut.services.ElifutService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.Executor;

import retrofit.ObservableCallAdapterFactory;
import retrofit.Retrofit;

final class NetworkModule {
  ElifutService provideService(Retrofit retrofit) {
    return retrofit.create(ElifutService.class);
  }

  Retrofit provideRetrofit(OkHttpClient client, HttpUrl baseUrl, Executor callbackExecutor) {
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .callbackExecutor(callbackExecutor)
        .callAdapterFactory(ObservableCallAdapterFactory.create())
        .build();
  }

  HttpUrl provideBaseUrl() {
    return HttpUrl.parse("http://10.0.3.2");
  }

  Cache provideCache(Context context) {
    File cacheDir = new File(context.getCacheDir(), "okhttp");
    long maxSize = 20L * 1024 * 1024;
    return new Cache(cacheDir, maxSize);
  }

  OkHttpClient provideOkHttpClient(Cache cache) {
    return new OkHttpClient().setCache(cache);
  }
}
