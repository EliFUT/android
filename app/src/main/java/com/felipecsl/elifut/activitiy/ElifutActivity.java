package com.felipecsl.elifut.activitiy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.ElifutComponent;
import com.felipecsl.elifut.services.ElifutService;

import javax.inject.Inject;

import icepick.Icepick;

public abstract class ElifutActivity extends AppCompatActivity {

  @Inject ElifutService service;

  protected ElifutApplication getElifutApp() {
    return (ElifutApplication) getApplication();
  }

  protected ElifutComponent daggerComponent() {
    return getElifutApp().component();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }
}
