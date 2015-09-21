package com.felipecsl.elifut.services;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Player;

import java.util.List;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface ElifutService {
  @GET("/players/{id}.json")
  Observable<Response<Player>> player(@Path("id") int id);

  @GET("/players/squad.json")
  Observable<Response<List<Player>>> clubPlayers(@Query("club_id") int clubId);

  @GET("/clubs/{id}.json")
  Observable<Response<Club>> club(@Path("id") int id);

  @GET("/clubs/random.json")
  Observable<Response<Club>> randomClub(@Query("nation_id") int nationId);

  @GET("/nations.json")
  Observable<Response<List<Nation>>> nations();

  @GET("/leagues/{id}.json")
  Observable<Response<League>> league(@Path("id") int id);

  @GET("/clubs.json")
  Observable<Response<List<Club>>> clubsByLeague(@Query("league_id") int leagueId);
}
