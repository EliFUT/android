package com.felipecsl.elifut;

import android.app.Application;

import com.felipecsl.elifut.services.ElifutService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;

public class ElifutApplication extends Application {
  private Retrofit retrofit;
  private OkHttpClient okHttpClient;
  private HttpUrl baseUrl;
  private ElifutService service;

  @Override public void onCreate() {
    super.onCreate();

    NetworkModule networkModule = new NetworkModule();
    Cache cache = networkModule.provideCache(this);
    okHttpClient = networkModule.provideOkHttpClient(cache);
    baseUrl = networkModule.provideBaseUrl();
    ConcurrentUtil.MainThreadExecutor mainThreadExecutor = new ConcurrentUtil.MainThreadExecutor();
    retrofit = networkModule.provideRetrofit(okHttpClient, baseUrl, mainThreadExecutor);
    service = networkModule.provideService(retrofit);
  }

  public Retrofit retrofit() {
    return retrofit;
  }

  public OkHttpClient okHttpClient() {
    return okHttpClient;
  }

  public HttpUrl baseUrl() {
    return baseUrl;
  }

  public ElifutService service() {
    return service;
  }
}
