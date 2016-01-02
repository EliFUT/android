package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;

public final class AutoValueClasses {
  public static final Class<? extends Club> CLUB = Util.autoValueTypeFor(Club.class);
  public static final Class<? extends Match> MATCH = Util.autoValueTypeFor(Match.class);
  public static final Class<? extends LeagueRound> LEAGUE_ROUND =
      Util.autoValueTypeFor(LeagueRound.class);
  public static final Class<? extends MatchResult> MATCH_RESULT =
      Util.autoValueTypeFor(MatchResult.class);

  private AutoValueClasses() {
  }
}
