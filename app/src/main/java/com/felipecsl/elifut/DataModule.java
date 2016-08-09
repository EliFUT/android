package com.felipecsl.elifut;

import android.content.Context;
import android.content.SharedPreferences;

import com.felipecsl.elifut.adapter.ModelListAdapterFactory;
import com.felipecsl.elifut.match.LeagueRoundExecutor;
import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.GoalGenerator;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.models.converter.ClubConverter;
import com.felipecsl.elifut.models.converter.ClubSquadConverter;
import com.felipecsl.elifut.models.converter.LeagueRoundConverter;
import com.felipecsl.elifut.models.converter.MatchConverter;
import com.felipecsl.elifut.models.converter.MatchResultConverter;
import com.felipecsl.elifut.models.converter.PlayerConverter;
import com.felipecsl.elifut.preferences.LeagueDetails;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.felipecsl.elifut.services.ClubDataStore;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.services.LeagueRoundGenerator;
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
        .add(new ModelListAdapterFactory<>(Nation.class))
        .add(new ModelListAdapterFactory<>(Club.class))
        .add(new ModelListAdapterFactory<>(Player.class))
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
