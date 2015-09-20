package com.felipecsl.elifut.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.SimpleResponseObserver;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.services.ElifutService;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit.Response;
import rx.android.schedulers.AndroidSchedulers;

public final class TeamPlayersFragment extends Fragment {
  private static final String TAG = TeamPlayersFragment.class.getSimpleName();
  private static final String EXTRA_CLUB = "EXTRA_CLUB";

  @Bind(R.id.recycler_players) RecyclerView playersList;
  @BindDimen(R.dimen.player_spacing) int playerSpacing;

  @Inject ElifutService service;

  @State Club club;
  @State Nation nation;

  public static TeamPlayersFragment newInstance(Club club) {
    TeamPlayersFragment fragment = new TeamPlayersFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable(EXTRA_CLUB, club);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    ElifutApplication application = (ElifutApplication) getActivity().getApplication();
    application.component().inject(this);

    View v = inflater.inflate(R.layout.fragment_team_players, container, false);

    ButterKnife.bind(this, v);
    Icepick.restoreInstanceState(this, savedInstanceState);

    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    playersList.setLayoutManager(layoutManager);

    if (savedInstanceState == null) {
      club = getArguments().getParcelable(EXTRA_CLUB);
    }

    loadPlayers();

    return v;
  }

  private void loadPlayers() {
    service.clubPlayers(club.base_id())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SimpleResponseObserver<List<Player>>() {
          @Override public void onError(Throwable e) {
            Toast.makeText(getActivity(), "Failed to load club players", Toast.LENGTH_SHORT).show();
            Log.w(TAG, e);
          }

          @Override public void onNext(Response<List<Player>> response) {
            List<Player> players = response.body();
            playersList.setAdapter(new PlayersAdapter(players, club, nation));
          }
        });
  }

  static class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {
    private final List<Player> players;
    private final Club club;
    private final Nation nation;

    PlayersAdapter(List<Player> players, Club club, Nation nation) {
      this.players = players;
      this.club = club;
      this.nation = nation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      holder.bind(players.get(position));
    }

    @Override public int getItemCount() {
      return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      @Bind(R.id.player_name) TextView txtPlayerName;
      @Bind(R.id.player_image) ImageView imgPlayer;
      @Bind(R.id.player_rating) TextView txtPlayerRating;
      @Bind(R.id.player_layout) FrameLayout playerLayout;
      @Bind(R.id.player_position) TextView playerPosition;
      @Bind(R.id.img_player_quality) ImageView imgPlayerQuality;
      @Bind(R.id.img_player_club) ImageView imgClub;
      @Bind(R.id.img_player_nation) ImageView imgNation;
      @Bind(R.id.attribute_1) TextView attribute1;
      @Bind(R.id.attribute_2) TextView attribute2;
      @Bind(R.id.attribute_3) TextView attribute3;
      @Bind(R.id.attribute_4) TextView attribute4;
      @Bind(R.id.attribute_5) TextView attribute5;
      @Bind(R.id.attribute_6) TextView attribute6;

      public ViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.player_item, parent, false));
        ButterKnife.bind(this, itemView);
      }

      public void bind(Player player) {
        Context context = itemView.getContext();
        int backgroundImageResId = context.getResources().getIdentifier(player.color(),
            "drawable", context.getPackageName());
        imgPlayerQuality.setImageResource(backgroundImageResId);
        txtPlayerRating.setText(String.valueOf(player.rating()));
        playerPosition.setText(player.position());
        txtPlayerName.setText(player.name().toUpperCase());

        attribute1.setText(createAttributeSpan(player.attribute_1(), "PAC"));
        attribute2.setText(createAttributeSpan(player.attribute_2(), "SHO"));
        attribute3.setText(createAttributeSpan(player.attribute_3(), "PAS"));
        attribute4.setText(createAttributeSpan(player.attribute_4(), "DRI"));
        attribute5.setText(createAttributeSpan(player.attribute_5(), "DEF"));
        attribute6.setText(createAttributeSpan(player.attribute_6(), "PHY"));

        Picasso.with(context)
            .load(player.remoteImage())
            .into(imgPlayer);

        Picasso.with(context)
            .load(club.remoteImageSmall())
            .into(imgClub);

        Picasso.with(context)
            .load(player.nationRemoteImageSmall())
            .into(imgNation);
      }
    }

    private Spannable createAttributeSpan(int attribute, String suffix) {
      SpannableString spannable = new SpannableString(String.valueOf(attribute) + " " + suffix);
      spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
      return spannable;
    }
  }
}
