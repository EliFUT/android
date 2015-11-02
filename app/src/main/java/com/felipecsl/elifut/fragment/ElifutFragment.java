package com.felipecsl.elifut.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.ElifutComponent;
import com.felipecsl.elifut.activitiy.ElifutActivity;
import com.felipecsl.elifut.services.ElifutService;

import javax.inject.Inject;

import icepick.Icepick;
import retrofit.Response;
import rx.Observable;

public abstract class ElifutFragment extends Fragment {

  @Inject ElifutService service;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  protected ElifutComponent daggerComponent() {
    ElifutApplication app = (ElifutApplication) getActivity().getApplication();
    return app.component();
  }

  protected <T> Observable.Transformer<Response<T>, T> applyTransformations() {
    ElifutActivity activity = (ElifutActivity) getActivity();
    return activity.applyTransformations();
  }
}
