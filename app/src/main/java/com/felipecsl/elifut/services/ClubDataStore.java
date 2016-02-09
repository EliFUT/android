package com.felipecsl.elifut.services;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.Player;

import java.util.List;

import rx.Observable;

public class ClubDataStore {
  private ElifutDataStore dataStore;

  public ClubDataStore(ElifutDataStore dataStore) {
    this.dataStore = dataStore;
  }

  public ClubSquad squad(Club club) {
    String id = String.valueOf(club.id());
    return dataStore.queryOne(AutoValueClasses.CLUB_SQUAD, "club_id = ?", id);
  }

  public List<? extends Player> allPlayers(Club club) {
    String id = String.valueOf(club.id());
    return dataStore.query(AutoValueClasses.PLAYER, "club_id = ?", id);
  }

  public void updateSquad(ClubSquad currentSquad, ClubSquad newSquad) {
    dataStore.update(newSquad, currentSquad.id());
  }

  public Observable<? extends ClubSquad> squadObservable(Club club) {
    String id = String.valueOf(club.id());
    return dataStore.observeOne(AutoValueClasses.CLUB_SQUAD, "club_id = ?", id);
  }
}
