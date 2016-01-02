package com.felipecsl.elifut.services;

import android.support.annotation.VisibleForTesting;

import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.felipecsl.elifut.util.CollectionUtils.shuffle;

public class LeagueRoundGenerator {
  private final MatchResultGenerator generator;

  public LeagueRoundGenerator() {
    this(new MatchResultGenerator());
  }

  public LeagueRoundGenerator(MatchResultGenerator generator) {
    this.generator = generator;
  }

  @VisibleForTesting List<LeagueRound> generateRoundsDeterministic(List<? extends Club> clubs) {
    int totalClubs = clubs.size();

    Preconditions.checkNotNull(clubs);
    Preconditions.checkArgument(totalClubs % 2 == 0, "Need even number of clubs");
    Preconditions.checkArgument(totalClubs > 1, "Need at least 2 clubs");

    int totalRounds = (totalClubs - 1) * 2;
    int matchesPerRound = totalClubs / 2;
    LeagueRound[] rounds = new LeagueRound[totalRounds];

    for (int k = 0; k < rounds.length; k++) {
      rounds[k] = LeagueRound.create(k + 1);
    }
    Map<Integer, Club> clubMap = new HashMap<>();
    for (int i = 1; i <= totalClubs; i++) {
      clubMap.put(i, clubs.get(i - 1));
    }

    for (int round = 0; round < totalRounds; round++) {
      for (int match = 0; match < matchesPerRound; match++) {
        int home = (round + match) % (totalClubs - 1);
        int away = (totalClubs - 1 - match + round) % (totalClubs - 1);

        // Last team stays in the same place while the others
        // rotate around it.
        if (match == 0) {
          away = totalClubs - 1;
        }

        Club clubHome;
        Club clubAway;
        if (round % 2 == 0) {
          clubHome = clubMap.get(home + 1);
          clubAway = clubMap.get(away + 1);
        }
        else {
          clubHome = clubMap.get(away + 1);
          clubAway = clubMap.get(home + 1);
        }

        Match newMatch = Match.create(clubHome, clubAway, generator.generate(clubHome, clubAway));
        rounds[round].addMatch(newMatch);
      }
    }

    return  Arrays.asList(rounds);
  }

  /** Generates a random list of league rounds from the provided list of clubs. */
  public List<LeagueRound> generateRounds(List<? extends Club> clubs) {
    return generateRoundsDeterministic(shuffle(clubs));
  }
}
