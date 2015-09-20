package com.felipecsl.elifut.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.SimpleTarget;
import com.felipecsl.elifut.activitiy.TeamDetailsActivity;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Nation;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public final class TeamDetailsFragment extends Fragment {
  private static final String EXTRA_CLUB = "EXTRA_CLUB";
  private static final String EXTRA_COACH_NAME = "COACH_NAME";
  private static final String EXTRA_NATION = "NATION";
  private final SimpleTarget target = new SimpleTarget() {
    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      imgClubLogo.setImageBitmap(bitmap);
      loadPalette(bitmap);
    }
  };

  @Bind(R.id.img_club_logo) ImageView imgClubLogo;
  @Bind(R.id.txt_club) TextView txtClub;
  @Bind(R.id.txt_nation) TextView txtNation;
  @Bind(R.id.txt_manager) TextView txtManager;
  @BindColor(R.color.color_primary) int colorPrimary;
  @BindColor(R.color.color_secondary) int colorSecondary;

  @State Club club;
  @State Nation nation;
  @State String coachName;

  public static Fragment newInstance(Club club, String coachName, Nation nation) {
    TeamDetailsFragment fragment = new TeamDetailsFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable(EXTRA_CLUB, club);
    bundle.putParcelable(EXTRA_NATION, nation);
    bundle.putString(EXTRA_COACH_NAME, coachName);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_team_details, container, false);

    ButterKnife.bind(this, v);
    Icepick.restoreInstanceState(this, savedInstanceState);

    if (savedInstanceState == null) {
      Bundle arguments = getArguments();
      club = arguments.getParcelable(EXTRA_CLUB);
      nation = arguments.getParcelable(EXTRA_NATION);
      coachName = arguments.getString(EXTRA_COACH_NAME);
    }

    txtClub.setText(club.name());
    txtNation.setText(nation.name());
    txtManager.setText(getString(R.string.manager_name, coachName));

    loadTeamLogo();

    return v;
  }

  private void loadTeamLogo() {
    Picasso.with(getActivity())
        .load(club.remoteImageLarge())
        .into(target);
  }

  private void loadPalette(Bitmap bitmap) {
    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
      public void onGenerated(Palette palette) {
        TeamDetailsActivity activity = (TeamDetailsActivity) getActivity();
        activity.setToolbarColor(
            palette.getDarkMutedColor(colorPrimary), palette.getVibrantColor(colorSecondary));
      }
    });
  }

  @OnClick(R.id.fab) public void onClickNext() {

  }
}
