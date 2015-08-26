package com.felipecsl.elifut;

import android.content.Context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.felipecsl.elifut.services.ElifutService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.Executor;

import retrofit.Converter;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

final class NetworkModule {
  ElifutService provideService(Retrofit retrofit) {
    return retrofit.create(ElifutService.class);
  }

  Retrofit provideRetrofit(OkHttpClient client, HttpUrl baseUrl, Executor callbackExecutor,
      Converter.Factory converterFactory) {
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .callbackExecutor(callbackExecutor)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(converterFactory)
        .build();
  }

  HttpUrl provideBaseUrl() {
    return HttpUrl.parse("http://10.0.3.2:3000/");
  }

  Converter.Factory provideConverterFactory(ObjectMapper objectMapper) {
    return JacksonConverterFactory.create(objectMapper);
  }

  ObjectMapper provideObjectMapper() {
    return new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
        .registerModules(new ServiceModule());
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
