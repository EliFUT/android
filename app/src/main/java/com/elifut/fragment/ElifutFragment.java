package com.elifut.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.elifut.ElifutApplication;
import com.elifut.ElifutComponent;
import com.elifut.preferences.LeagueDetails;
import com.elifut.preferences.UserPreferences;
import com.elifut.services.ElifutService;

import javax.inject.Inject;

import icepick.Icepick;

public abstract class ElifutFragment extends Fragment {
  @Inject ElifutService service;
  @Inject UserPreferences userPreferences;
  @Inject LeagueDetails leagueDetails;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
    daggerComponent().inject(this);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  protected ElifutComponent daggerComponent() {
    ElifutApplication app = (ElifutApplication) getActivity().getApplication();
    return app.component();
  }
}
