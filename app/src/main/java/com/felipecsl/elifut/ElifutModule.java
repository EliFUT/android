package com.felipecsl.elifut;

import android.content.SharedPreferences;

import com.felipecsl.elifut.preferences.LeagueDetails;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.services.ElifutService;

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
