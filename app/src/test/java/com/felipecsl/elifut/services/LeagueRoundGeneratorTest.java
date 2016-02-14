package com.felipecsl.elifut.services;

import com.felipecsl.elifut.TestFixtures;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.felipecsl.elifut.TestUtil.newClub;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class LeagueRoundGeneratorTest {
  @Test public void testGenerateRoundsThrowsForEmptyListOfClubs() {
    LeagueRoundGenerator roundGenerator = new LeagueRoundGenerator();
    try {
      roundGenerator.generateRounds(Collections.emptyList());
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test public void testGenerateRoundsOddNumberOfClubsDropsOne() {
    LeagueRoundGenerator roundGenerator = new LeagueRoundGenerator();
    List<LeagueRound> leagueRounds = roundGenerator.generateRounds(
        Arrays.asList(newClub("Gremio"), newClub("Internacional"), newClub("Juventude")));

    assertThat(leagueRounds.size()).isEqualTo(2);
  }

  @Test public void testGenerateRoundsTwoClubs() {
    Club gremio = TestFixtures.GREMIO;
    Club internacional = TestFixtures.INTERNACIONAL;
    List<Club> clubs = Arrays.asList(gremio, internacional);

    LeagueRoundGenerator roundGenerator = new LeagueRoundGenerator();

    List<LeagueRound> leagueRounds = roundGenerator.generateRoundsDeterministic(clubs);

    assertThat(leagueRounds.size()).isEqualTo(2);
    assertThat(leagueRounds).isEqualTo(Arrays.asList(
        LeagueRound.create(1, singletonList(Match.create(gremio, internacional))),
        LeagueRound.create(2, singletonList(Match.create(internacional, gremio)))));
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
    LeagueRoundGenerator roundGenerator = new LeagueRoundGenerator();

    List<LeagueRound> leagueRounds = roundGenerator.generateRounds(clubs);

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
}