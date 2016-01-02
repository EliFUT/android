package com.felipecsl.elifut;

import com.felipecsl.elifut.match.LeagueRoundExecutorTest;
import com.felipecsl.elifut.models.factory.ClubConverterTest;
import com.felipecsl.elifut.models.factory.LeagueRoundConverterTest;
import com.felipecsl.elifut.models.factory.MatchConverterTest;
import com.felipecsl.elifut.preferences.LeagueDetailsTest;
import com.felipecsl.elifut.services.ElifutPersistenceServiceTest;

public interface TestElifutGraph {
  void inject(MatchResultControllerTest matchResultsControllerTest);
  void inject(LeagueRoundExecutorTest leagueRoundExecutorTest);
  void inject(LeagueRoundConverterTest leagueRoundConverterTest);
  void inject(ClubConverterTest clubConverterTest);
  void inject(MatchConverterTest matchConverterTest);
  void inject(ElifutPersistenceServiceTest elifutPersistenceServiceTest);
  void inject(LeagueDetailsTest leagueDetailsTest);
}
