package com.felipecsl.elifut.models;

import android.support.annotation.Nullable;

import com.felipecsl.elifut.match.MatchResultGenerator;
import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.squareup.moshi.JsonAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoValue
public abstract class League extends Model {
  public abstract String name();

  @Nullable public abstract String abbrev_name();

  public abstract String image();

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_League.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }

  public static List<LeagueRound> generateRounds(List<Club> clubs) {
    return generateRounds(clubs, new MatchResultGenerator());
  }

  /**
   * Generates a list of league rounds from the provided list of clubs. Assumes that the list is
   * already randomized.
   */
  public static List<LeagueRound> generateRounds(List<Club> clubs, MatchResultGenerator generator) {
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
}
