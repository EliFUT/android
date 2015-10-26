package com.felipecsl.elifut;

import android.content.Context;

import com.felipecsl.elifut.services.ElifutService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.CallAdapter;
import retrofit.Converter;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class NetworkModule {
  private final Context context;

  public NetworkModule(Context context) {
    this.context = context;
  }

  @Provides @Singleton ElifutService provideService(Retrofit retrofit) {
    return retrofit.create(ElifutService.class);
  }

  @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client, HttpUrl baseUrl,
      Executor callbackExecutor, Converter.Factory converterFactory, CallAdapter.Factory factory) {
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .callbackExecutor(callbackExecutor)
        .addCallAdapterFactory(factory)
        .addConverterFactory(converterFactory)
        .build();
  }

  @Provides @Singleton Executor provideExecutor() {
    return new ConcurrentUtil.MainThreadExecutor();
  }

  @Provides @Singleton CallAdapter.Factory provideCallAdapterFactory() {
    return RxJavaCallAdapterFactory.create();
  }

  @Provides @Singleton HttpUrl provideBaseUrl() {
    return HttpUrl.parse("http://10.0.3.2:3000/");
  }

  @Provides @Singleton Cache provideCache() {
    File cacheDir = new File(context.getCacheDir(), "okhttp");
    long maxSize = 20L * 1024 * 1024;
    return new Cache(cacheDir, maxSize);
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(Cache cache) {
    OkHttpClient client = new OkHttpClient();
    StethoInitializer.addInterceptor(client.networkInterceptors());
    return client.setCache(cache);
  }
}
