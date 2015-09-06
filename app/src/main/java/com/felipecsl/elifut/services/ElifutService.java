package com.felipecsl.elifut.services;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Player;

import java.util.List;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ElifutService {
  @GET("/players/{id}.json") Observable<Response<Player>> player(int id);
  @GET("/clubs/{id}.json") Observable<Response<Club>> club(int id);
  @GET("/clubs/random.json") Observable<Response<Club>> randomClub(@Query("nation_id") int nationId);
  @GET("/nations.json") Observable<Response<List<Nation>>> nations();
}
