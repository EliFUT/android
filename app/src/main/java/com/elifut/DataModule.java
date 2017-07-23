package com.elifut;

import android.content.Context;
import android.content.SharedPreferences;

import com.elifut.match.LeagueRoundExecutor;
import com.elifut.match.MatchResultGenerator;
import com.elifut.models.GoalGenerator;
import com.elifut.models.Persistable;
import com.elifut.models.converter.ClubConverter;
import com.elifut.models.converter.ClubSquadConverter;
import com.elifut.models.converter.LeagueRoundConverter;
import com.elifut.models.converter.MatchConverter;
import com.elifut.models.converter.MatchResultConverter;
import com.elifut.models.converter.PlayerConverter;
import com.elifut.preferences.LeagueDetails;
import com.elifut.preferences.UserPreferences;
import com.elifut.services.ClubDataStore;
import com.elifut.services.ElifutDataStore;
import com.elifut.services.LeagueRoundGenerator;
import com.squareup.moshi.Moshi;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class DataModule {
  private final Context context;

  public DataModule(Context context) {
    this.context = context;
  }

  @Provides @Singleton Converter.Factory provideConverterFactory(Moshi moshi) {
    return MoshiConverterFactory.create(moshi);
  }

  @Provides @Singleton Moshi provideMoshi() {
    return new Moshi.Builder()
        .add(AutoValueMoshiAdapterFactory.create())
        .build();
  }

  @Provides @Singleton SharedPreferences provideSharedPreferences() {
    return context.getSharedPreferences("ELIFUT_DATA", Context.MODE_PRIVATE);
  }

  @Provides @Singleton UserPreferences provideUserPreferences(SharedPreferences preferences,
      Moshi moshi) {
    return new UserPreferences(preferences, moshi);
  }

  @Provides @Singleton LeagueRoundGenerator provideLeagueRoundGenerator() {
    return new LeagueRoundGenerator();
  }

  @Provides @Singleton GoalGenerator provideGoalGenerator(ClubDataStore clubDataStore) {
    return new GoalGenerator(clubDataStore);
  }

  @Provides @Singleton MatchResultGenerator provideMatchResultGenerator(
      GoalGenerator goalGenerator) {
    return new MatchResultGenerator(goalGenerator);
  }

  @Provides @Singleton LeagueDetails provideLeagueDetails(ClubDataStore clubDataStore,
      LeagueRoundGenerator leagueRoundGenerator, MatchResultGenerator matchResultGenerator,
      ElifutDataStore persistenceService) {
    return new LeagueDetails(persistenceService, clubDataStore, leagueRoundGenerator,
        matchResultGenerator);
  }

  @Provides @Singleton ClubDataStore provideClubDataStore(ElifutDataStore elifutDataStore) {
    return new ClubDataStore(elifutDataStore);
  }

  @Provides @Singleton ElifutDataStore provideDataStore(List<Persistable.Converter<?>> converters) {
    return new ElifutDataStore(context, SqlBrite.create(), converters);
  }

  @Provides @Singleton List<Persistable.Converter<?>> providePersistenceConverters(Moshi moshi) {
    return Arrays.asList(
        new ClubConverter(),
        new MatchConverter(),
        new ClubSquadConverter(),
        new MatchResultConverter(moshi),
        new LeagueRoundConverter(),
        new PlayerConverter());
  }

  @Provides @Singleton LeagueRoundExecutor provideLeagueRoundExecutor(
      ElifutDataStore persistenceService) {
    return new LeagueRoundExecutor(persistenceService);
  }
}
