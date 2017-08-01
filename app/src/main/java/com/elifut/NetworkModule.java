package com.elifut;

import android.content.Context;

import com.elifut.services.ElifutService;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okreplay.OkReplayInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

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

  @Provides @Singleton OkReplayInterceptor provideOkReplayInterceptor() {
    return OkReplayInterceptorProvider.Companion.getInstance();
  }

  @Provides @Singleton CallAdapter.Factory provideCallAdapterFactory() {
    return RxJavaCallAdapterFactory.create();
  }

  @Provides @Singleton HttpUrl provideBaseUrl() {
    return HttpUrl.parse(BuildConfig.API_ENDPOINT);
  }

  @Provides @Singleton Cache provideCache() {
    File cacheDir = new File(context.getCacheDir(), "okhttp");
    long maxSize = 20L * 1024 * 1024;
    return new Cache(cacheDir, maxSize);
  }

  @Provides @Singleton static OkHttpClient provideOkHttpClient(Cache cache,
      OkReplayInterceptor okReplayInterceptor) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor(okReplayInterceptor);
    StethoInitializer.addInterceptor(builder.networkInterceptors());
    return builder.build();
  }
}
