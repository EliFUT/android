package com.elifut;

import android.content.Context;

import com.elifut.activity.CurrentTeamDetailsActivity;
import com.elifut.activity.ElifutActivity;
import com.elifut.activity.MainActivity;
import com.elifut.activity.MatchProgressActivity;
import com.elifut.activity.NavigationActivity;
import com.elifut.activity.TeamPlayersActivity;
import com.elifut.fragment.ElifutFragment;
import com.elifut.fragment.LeagueProgressFragment;
import com.elifut.fragment.LeagueStandingsFragment;
import com.elifut.fragment.TeamDetailsFragment;
import com.elifut.fragment.TeamSquadFragment;
import com.elifut.preferences.UserPreferences;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import okreplay.OkReplayInterceptor;

@Singleton
@Component(modules = {
    NetworkModule.class,
    DataModule.class,
    ElifutModule.class,
    AnalyticsModule.class
})
public interface ElifutComponent {
  OkHttpClient okHttpClient();
  AppInitializer appInitializer();
  UserPreferences userPreferences();

  void inject(MainActivity mainActivity);
  void inject(CurrentTeamDetailsActivity currentTeamDetailsActivity);
  void inject(TeamPlayersActivity activity);
  void inject(TeamDetailsFragment teamDetailsFragment);
  void inject(LeagueStandingsFragment leagueStandingsActivity);
  void inject(MatchProgressActivity matchProgressActivity);
  void inject(LeagueProgressFragment leagueProgressFragment);
  void inject(NavigationActivity navigationActivity);
  void inject(ElifutFragment elifutFragment);
  void inject(TeamSquadFragment elifutFragment);
  void inject(ElifutActivity elifutActivity);

  final class Initializer {
    private Initializer() {
    }

    static ElifutComponent init(Context context) {
      return DaggerElifutComponent.builder()
          .networkModule(new NetworkModule(context))
          .dataModule(new DataModule(context))
          .analyticsModule(new AnalyticsModule(context))
          .build();
    }
  }
}
