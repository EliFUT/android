package com.felipecsl.elifut;

import android.content.Context;

import com.felipecsl.elifut.activitiy.CurrentTeamDetailsActivity;
import com.felipecsl.elifut.activitiy.ElifutActivity;
import com.felipecsl.elifut.activitiy.MainActivity;
import com.felipecsl.elifut.activitiy.MatchProgressActivity;
import com.felipecsl.elifut.activitiy.NavigationActivity;
import com.felipecsl.elifut.fragment.ElifutFragment;
import com.felipecsl.elifut.fragment.LeagueProgressFragment;
import com.felipecsl.elifut.fragment.LeagueStandingsFragment;
import com.felipecsl.elifut.fragment.TeamDetailsFragment;
import com.felipecsl.elifut.fragment.TeamPlayersFragment;
import com.felipecsl.elifut.fragment.TeamSquadFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { NetworkModule.class, DataModule.class, ElifutModule.class })
public interface ElifutComponent {
  void inject(MainActivity mainActivity);
  void inject(CurrentTeamDetailsActivity currentTeamDetailsActivity);
  void inject(TeamPlayersFragment teamPlayersFragment);
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
          .elifutModule(new ElifutModule())
          .networkModule(new NetworkModule(context))
          .dataModule(new DataModule(context))
          .build();
    }
  }
}
