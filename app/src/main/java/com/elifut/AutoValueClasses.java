package com.elifut;

import com.elifut.models.Club;
import com.elifut.models.ClubSquad;
import com.elifut.models.LeagueRound;
import com.elifut.models.Match;
import com.elifut.models.MatchResult;
import com.elifut.models.Player;

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
