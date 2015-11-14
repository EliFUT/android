package com.felipecsl.elifut;

import android.content.Context;

import com.felipecsl.elifut.fragment.ElifutFragment;
import com.felipecsl.elifut.fragment.LeagueStandingsFragment;
import com.felipecsl.elifut.activitiy.NavigationActivity;
import com.felipecsl.elifut.fragment.LeagueProgressFragment;
import com.felipecsl.elifut.activitiy.MainActivity;
import com.felipecsl.elifut.activitiy.MatchProgressActivity;
import com.felipecsl.elifut.activitiy.TeamDetailsActivity;
import com.felipecsl.elifut.fragment.TeamDetailsFragment;
import com.felipecsl.elifut.fragment.TeamPlayersFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { NetworkModule.class, DataModule.class })
public interface ElifutComponent {
  void inject(MainActivity mainActivity);
  void inject(TeamDetailsActivity teamDetailsActivity);
  void inject(TeamPlayersFragment teamPlayersFragment);
  void inject(TeamDetailsFragment teamDetailsFragment);
  void inject(LeagueStandingsFragment leagueStandingsActivity);
  void inject(MatchProgressActivity matchProgressActivity);
  void inject(LeagueProgressFragment leagueProgressFragment);
  void inject(NavigationActivity navigationActivity);
  void inject(ElifutFragment elifutFragment);

  final class Initializer {
    private Initializer() {
    }

    static ElifutComponent init(Context context) {
      return DaggerElifutComponent.builder()
          .networkModule(new NetworkModule(context))
          .dataModule(new DataModule(context))
          .build();
    }
  }
}
