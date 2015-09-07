package com.felipecsl.elifut.activitiy;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.adapter.CountriesSpinnerAdapter;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.services.ElifutService;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Response;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.input_name) EditText inputName;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.countries_spinner) Spinner countriesSpinner;

  @Inject ElifutService service;
  private CountriesSpinnerAdapter nationsAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    collapsingToolbar.setTitle(getTitle());

    ElifutApplication application = (ElifutApplication) getApplication();
    application.component().inject(this);
    service.nations()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SimpleResponseObserver<List<Nation>>() {
          @Override public void onError(Throwable throwable) {
            Toast.makeText(MainActivity.this, "Failed to load list of countries",
                Toast.LENGTH_SHORT).show();
            Log.w(TAG, throwable);
          }

          @Override public void onNext(Response<List<Nation>> nations) {
            nationsAdapter = new CountriesSpinnerAdapter(MainActivity.this, nations.body());
            countriesSpinner.setAdapter(nationsAdapter);
          }
        });
  }

  @OnClick(R.id.btn_next) public void onClickNext() {
    Nation nation = (Nation) nationsAdapter.getItem(countriesSpinner.getSelectedItemPosition());
    startActivity(TeamDetailsActivity.newIntent(this, nation, inputName.getText().toString()));
  }
}
