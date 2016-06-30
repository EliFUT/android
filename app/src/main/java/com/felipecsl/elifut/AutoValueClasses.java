package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.Player;

public final class AutoValueClasses {
  public static final Class<Club> CLUB = Util.autoValueTypeFor(Club.class);
  public static final Class<Match> MATCH = Util.autoValueTypeFor(Match.class);
  public static final Class<Player> PLAYER = Util.autoValueTypeFor(Player.class);
  public static final Class<LeagueRound> LEAGUE_ROUND = Util.autoValueTypeFor(LeagueRound.class);
  public static final Class<MatchResult> MATCH_RESULT = Util.autoValueTypeFor(MatchResult.class);
  public static final Class<ClubSquad> CLUB_SQUAD = Util.autoValueTypeFor(ClubSquad.class);

  private AutoValueClasses() {
  }
}
