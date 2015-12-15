package com.felipecsl.elifut;

import android.content.Context;
import android.content.SharedPreferences;

import com.felipecsl.elifut.adapter.ModelListAdapterFactory;
import com.felipecsl.elifut.match.LeagueRoundExecutor;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubStats;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Converter;
import retrofit.MoshiConverterFactory;

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

  @Provides @Singleton
  UserPreferences provideUserPreferences(SharedPreferences preferences, Moshi moshi) {
    return new UserPreferences(preferences, moshi);
  }

  @Provides @Singleton
  LeaguePreferences provideLeaguePreferences(SharedPreferences preferences, Moshi moshi) {
    return new LeaguePreferences(preferences, moshi);
  }

  @Provides @Singleton
  LeagueRoundExecutor provideLeagueRoundExecutor(LeaguePreferences leaguePreferences) {
    return new LeagueRoundExecutor(leaguePreferences);
  }
}
