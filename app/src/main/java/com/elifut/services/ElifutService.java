package com.elifut.services;

import com.elifut.models.Club;
import com.elifut.models.League;
import com.elifut.models.Nation;
import com.elifut.models.Player;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ElifutService {
  @GET("/players/{id}.json")
  Observable<Response<Player>> player(@Path("id") int id);

  @GET("/players/squad.json")
  Observable<Response<List<Player>>> playersByClub(@Query("club_id") int clubId);

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
