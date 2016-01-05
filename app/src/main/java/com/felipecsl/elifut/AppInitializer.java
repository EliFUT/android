package com.felipecsl.elifut;

import android.app.ProgressDialog;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.preferences.JsonPreference;
import com.felipecsl.elifut.preferences.LeagueDetails;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.felipecsl.elifut.services.ElifutPersistenceService;
import com.felipecsl.elifut.services.ElifutService;
import com.felipecsl.elifut.services.ResponseBodyMapper;
import com.felipecsl.elifut.services.ResponseMapper;
import com.google.common.collect.FluentIterable;

import java.util.List;

import retrofit.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

/** Makes all the API calls and saves all the state and data needed for the app to start up */
public class AppInitializer {
  private final ElifutService service;
  private final LeagueDetails leagueDetails;
  private final JsonPreference<Club> clubPreference;
  private final JsonPreference<League> leaguePreference;
  private final ElifutPersistenceService persistenceService;

  public AppInitializer(ElifutService service, UserPreferences userPreferences,
      LeagueDetails leagueDetails, ElifutPersistenceService persistenceService) {
    this.service = service;
    this.leagueDetails = leagueDetails;
    this.persistenceService = persistenceService;
    this.clubPreference = userPreferences.clubPreference();
    this.leaguePreference = userPreferences.leaguePreference();
  }

  public Observable<Void> initialize(int nationId, ProgressDialog progressDialog) {
    return service.randomClub(nationId)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .compose(transform())
        .flatMap(club -> {
          progressDialog.setProgress(15);
          return service.league(clubPreference.set(club).league_id())
              .compose(transform());
        })
        .flatMap(league -> {
          progressDialog.setProgress(30);
          return service.clubsByLeague(leaguePreference.set(league).id())
              .compose(transform());
        })
        .flatMap(clubs -> {
          progressDialog.setProgress(45);
          leagueDetails.initialize(clubs);
          return Observable.from(clubs);
        })
        .flatMap(club -> {
          progressDialog.setProgress(progressDialog.getProgress() + 2);
          return service.playersByClub(club.id())
              .compose(transform())
              .map(players -> new ClubAndPlayers(club, players));
        })
        .flatMap(clubAndPlayers -> {
          persistenceService.create(FluentIterable
              .from(clubAndPlayers.players)
              .transform(player -> player.toBuilder().clubId(clubAndPlayers.club.id()).build())
              .toList());
          return Observable.empty();
        });
  }

  private static <T> Observable.Transformer<Response<T>, T> transform() {
    return (Observable<Response<T>> observable) ->
        observable.flatMap(ResponseMapper.<T>instance())
            .map(ResponseBodyMapper.<T>instance());
  }

  static class ClubAndPlayers {
    final Club club;
    final List<Player> players;

    ClubAndPlayers(Club club, List<Player> players) {
      this.club = club;
      this.players = players;
    }
  }
}
