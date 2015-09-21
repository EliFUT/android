package com.felipecsl.elifut.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.ElifutComponent;
import com.felipecsl.elifut.services.ElifutService;

import javax.inject.Inject;

import icepick.Icepick;

public abstract class ElifutFragment extends Fragment {

  @Inject ElifutService service;

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  protected ElifutComponent daggerComponent() {
    ElifutApplication app = (ElifutApplication) getActivity().getApplication();
    return app.component();
  }
}
