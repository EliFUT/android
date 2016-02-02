package com.felipecsl.elifut;

import com.felipecsl.elifut.match.LeagueRoundExecutorTest;
import com.felipecsl.elifut.models.ClubTest;
import com.felipecsl.elifut.models.converter.ClubConverterTest;
import com.felipecsl.elifut.models.converter.LeagueRoundConverterTest;
import com.felipecsl.elifut.models.converter.MatchConverterTest;
import com.felipecsl.elifut.preferences.LeagueDetailsTest;
import com.felipecsl.elifut.services.ElifutDataStoreTest;

public interface TestElifutGraph {
  void inject(MatchResultControllerTest matchResultsControllerTest);
  void inject(LeagueRoundExecutorTest leagueRoundExecutorTest);
  void inject(LeagueRoundConverterTest leagueRoundConverterTest);
  void inject(ClubConverterTest clubConverterTest);
  void inject(MatchConverterTest matchConverterTest);
  void inject(ElifutDataStoreTest elifutPersistenceServiceTest);
  void inject(LeagueDetailsTest leagueDetailsTest);
  void inject(ClubTest clubTest);
}
