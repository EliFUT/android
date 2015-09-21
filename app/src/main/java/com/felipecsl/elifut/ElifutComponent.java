package com.felipecsl.elifut;

import android.content.Context;

import com.felipecsl.elifut.activitiy.LeagueDetailsActivity;
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
  void inject(LeagueDetailsActivity leagueDetailsActivity);
  void inject(MatchProgressActivity matchProgressActivity);

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
