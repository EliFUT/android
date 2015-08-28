package com.felipecsl.elifut;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { NetworkModule.class, DataModule.class })
public interface ElifutComponent {
  void inject(MainActivity mainActivity);

  final class Initializer {
    private Initializer() {
    }

    static ElifutComponent init(Context context) {
      return DaggerElifutComponent.builder()
          .networkModule(new NetworkModule(context))
          .dataModule(new DataModule())
          .build();
    }
  }
}
