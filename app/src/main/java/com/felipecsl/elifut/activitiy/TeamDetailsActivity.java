package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.services.ElifutService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class TeamDetailsActivity extends AppCompatActivity {
  private static final String EXTRA_COUNTRY = "EXTRA_COUNTRY";
  private static final String EXTRA_NAME = "EXTRA_NAME";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

  @State Nation nation;
  @State String coachName;

  @Inject ElifutService service;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team_details);

    Icepick.restoreInstanceState(this, savedInstanceState);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      coachName = intent.getStringExtra(EXTRA_NAME);
      nation = intent.getParcelableExtra(EXTRA_COUNTRY);
    }

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    collapsingToolbar.setTitle(nation.name());

    ElifutApplication application = (ElifutApplication) getApplication();
    application.component().inject(this);

    service.club()
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  public static Intent newIntent(Context context, Nation nation, String name) {
    return new Intent(context, TeamDetailsActivity.class)
        .putExtra(EXTRA_COUNTRY, nation)
        .putExtra(EXTRA_NAME, name);
  }
}
