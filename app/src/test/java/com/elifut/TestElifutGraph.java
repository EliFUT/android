package com.elifut;

import com.elifut.match.LeagueRoundExecutorTest;
import com.elifut.models.ClubTest;
import com.elifut.models.GoalGeneratorTest;
import com.elifut.models.converter.ClubConverterTest;
import com.elifut.models.converter.LeagueRoundConverterTest;
import com.elifut.models.converter.MatchConverterTest;
import com.elifut.preferences.LeagueDetailsTest;
import com.elifut.services.ElifutDataStoreTest;

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
