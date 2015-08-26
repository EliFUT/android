package com.felipecsl.elifut;

import android.app.Application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipecsl.elifut.services.ElifutService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Converter;
import retrofit.Retrofit;

public class ElifutApplication extends Application {
  private Retrofit retrofit;
  private OkHttpClient client;
  private HttpUrl baseUrl;
  private ElifutService service;

  @Override public void onCreate() {
    super.onCreate();

    NetworkModule networkModule = new NetworkModule();
    Cache cache = networkModule.provideCache(this);
    client = networkModule.provideOkHttpClient(cache);
    baseUrl = networkModule.provideBaseUrl();
    ConcurrentUtil.MainThreadExecutor mainThreadExecutor = new ConcurrentUtil.MainThreadExecutor();
    ObjectMapper objectMapper = networkModule.provideObjectMapper();
    Converter.Factory converterFactory = networkModule.provideConverterFactory(objectMapper);
    retrofit = networkModule.provideRetrofit(client, baseUrl, mainThreadExecutor, converterFactory);
    service = networkModule.provideService(retrofit);
  }

  public Retrofit retrofit() {
    return retrofit;
  }

  public OkHttpClient okHttpClient() {
    return client;
  }

  public HttpUrl baseUrl() {
    return baseUrl;
  }

  public ElifutService service() {
    return service;
  }
}
