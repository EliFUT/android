package com.felipecsl.elifut;

import android.content.Context;
import android.content.SharedPreferences;

import com.felipecsl.elifut.adapter.ModelListAdapterFactory;
import com.felipecsl.elifut.match.LeagueRoundExecutor;
import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubStats;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
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
import retrofit2.MoshiConverterFactory;

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
        .add(Nation.typeAdapterFactory())
        .add(Player.typeAdapterFactory())
        .add(League.typeAdapterFactory())
        .add(ClubStats.typeAdapterFactory())
        .add(Club.typeAdapterFactory())
        .add(LeagueRound.typeAdapterFactory())
        .add(MatchResult.typeAdapterFactory())
        .add(Goal.typeAdapterFactory())
        .add(Match.typeAdapterFactory())
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
    return new LeagueRoundGenerator(new MatchResultGenerator());
  }

  @Provides @Singleton LeagueDetails provideLeaguePreferences(
      ElifutDataStore persistenceService, LeagueRoundGenerator leagueRoundGenerator) {
    return new LeagueDetails(persistenceService, leagueRoundGenerator);
  }

  @Provides @Singleton ElifutDataStore provideElifutPersistenceService(
      List<Persistable.Converter<?>> converters) {
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
