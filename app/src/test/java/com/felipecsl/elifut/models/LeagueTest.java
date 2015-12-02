package com.felipecsl.elifut.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class LeagueTest {
  @Test public void testGenerateRoundsThrowsForEmptyListOfClubs() {
    try {
      League.generateRounds(Collections.emptyList());
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test public void testGenerateRoundsThrowsForOddNumberOfClubs() {
    try {
      League.generateRounds(Collections.singletonList(newClub("Gremio")));
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test public void testGenerateRoundsTwoClubs() {
    Club gremio = newClub("Gremio");
    Club internacional = newClub("Internacional");
    List<Club> clubs = Arrays.asList(gremio, internacional);

    List<LeagueRound> leagueRounds = League.generateRounds(clubs);

    assertThat(leagueRounds.size()).isEqualTo(2);
    assertThat(leagueRounds).isEqualTo(Arrays.asList(
        LeagueRound.create(1, Collections.singletonList(Match.create(gremio, internacional))),
        LeagueRound.create(2, Collections.singletonList(Match.create(internacional, gremio)))));
  }

  @Test public void testGenerateRounds() {
    Club gre = newClub("Gremio");
    Club inter = newClub("Internacional");
    Club fla = newClub("Flamengo");
    Club flu = newClub("Fluminense");
    Club sp = newClub("São Paulo");
    Club cor = newClub("Corinthians");
    Club atl = newClub("Atlético Mineiro");
    Club cru = newClub("Cruzeiro");
    Club san = newClub("Santos");
    Club pal = newClub("Palmeiras");
    List<Club> clubs = Arrays.asList(gre, inter, fla, flu, sp, cor, atl, cru, san, pal);

    List<LeagueRound> leagueRounds = League.generateRounds(clubs);

    List<Match> allMatches = new ArrayList<>(90);

    for (int i = 0; i < clubs.size(); i++) {
      for (int j = i + 1; j < clubs.size(); j++) {
        allMatches.add(Match.create(clubs.get(i), clubs.get(j)));
        allMatches.add(Match.create(clubs.get(j), clubs.get(i)));
      }
    }

    assertThat(leagueRounds.size()).isEqualTo(18);

    for (LeagueRound leagueRound : leagueRounds) {
      for (Match match : leagueRound.matches()) {
        assertThat(allMatches.remove(match)).isTrue();
      }
    }

    assertThat(allMatches).isEmpty();
  }

  private static Club newClub(String name) {
    return Club.builder()
        .name(name)
        .small_image("")
        .large_image("")
        .league_id(1)
        .id(1)
        .base_id(1)
        .build();
  }
}