package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Nation;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Converter;
import retrofit.MoshiConverterFactory;

@Module
public class DataModule {

  @Provides @Singleton Converter.Factory provideConverterFactory(Moshi moshi) {
    return MoshiConverterFactory.create(moshi);
  }

  @Provides @Singleton Moshi provideMoshi() {
    return new Moshi.Builder()
        .add(new NationListAdapterFactory())
        .add(Nation.typeAdapterFactory())
        .build();
  }
}
