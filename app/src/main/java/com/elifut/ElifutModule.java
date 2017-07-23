package com.elifut;

import android.content.SharedPreferences;

import com.elifut.preferences.LeagueDetails;
import com.elifut.preferences.UserPreferences;
import com.elifut.services.ElifutDataStore;
import com.elifut.services.ElifutService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ElifutModule {
  @Provides @Singleton public AppInitializer provideAppInitializer(ElifutService service,
      UserPreferences userPreferences, LeagueDetails leagueDetails,
      ElifutDataStore persistenceService, SharedPreferences sharedPreferences) {
    return new AppInitializer(service, userPreferences, leagueDetails, persistenceService,
        sharedPreferences);
  }
}
