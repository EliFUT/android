package com.felipecsl.elifut.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.activitiy.PlayerDetailsActivity;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Player;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerViewHolder extends BaseViewHolder<Player> {
  private final Club club;

  @Bind(R.id.player_outer_layout) ViewGroup outerLayout;
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

  protected PlayerViewHolder(ViewGroup parent, Club club) {
    this(parent, club, R.layout.player_item);
  }

  public PlayerViewHolder(ViewGroup parent, Club club, int layoutId) {
    super(parent, layoutId);
    this.club = club;
    ButterKnife.bind(this, itemView);
  }

  @Override public void bind(Player player) {
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
        .load(player.image())
        .into(imgPlayer);

    String clubImage = this instanceof LargePlayerViewHolder
        ? club.large_image()
        : club.small_image();

    Picasso.with(context)
        .load(clubImage)
        .into(imgClub);

    Picasso.with(context)
        .load(player.nation_image())
        .into(imgNation);

    if (shouldHandleClick()) {
      outerLayout.setOnClickListener(view -> {
        //noinspection unchecked
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            (Activity) context,
            Pair.create(imgPlayer, "player_image"),
            Pair.create(imgClub, "img_player_club"),
            Pair.create(imgNation, "img_player_nation"),
            Pair.create(imgPlayerQuality, "img_player_quality"));
        Intent intent = PlayerDetailsActivity.newIntent(context, player, club);
        context.startActivity(intent, options.toBundle());
      });
    }
  }

  protected boolean shouldHandleClick() {
    return true;
  }

  private Spannable createAttributeSpan(int attribute, String suffix) {
    SpannableString spannable = new SpannableString(String.valueOf(attribute) + " " + suffix);
    spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
    return spannable;
  }
}
