package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Player;
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
        .add(new ModelListAdapterFactory<>(Nation.class))
        .add(new ModelListAdapterFactory<>(Club.class))
        .add(new ModelListAdapterFactory<>(Player.class))
        .add(Nation.typeAdapterFactory())
        .add(Club.typeAdapterFactory())
        .add(Player.typeAdapterFactory())
        .add(League.typeAdapterFactory())
        .build();
  }
}
