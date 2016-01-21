package com.felipecsl.elifut.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Player;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerViewHolder extends BaseViewHolder<Player> {
  protected final Club club;

  @Bind(R.id.player_outer_layout) protected ViewGroup outerLayout;
  @Bind(R.id.player_name) protected TextView txtPlayerName;
  @Bind(R.id.player_image) protected ImageView imgPlayer;
  @Bind(R.id.player_rating) protected TextView txtPlayerRating;
  @Bind(R.id.player_layout) protected FrameLayout playerLayout;
  @Bind(R.id.player_position) protected TextView playerPosition;
  @Bind(R.id.img_player_quality) protected ImageView imgPlayerQuality;
  @Bind(R.id.img_player_club) protected ImageView imgClub;
  @Bind(R.id.img_player_nation) protected ImageView imgNation;
  @Bind(R.id.attribute_1) protected TextView attribute1;
  @Bind(R.id.attribute_2) protected TextView attribute2;
  @Bind(R.id.attribute_3) protected TextView attribute3;
  @Bind(R.id.attribute_4) protected TextView attribute4;
  @Bind(R.id.attribute_5) protected TextView attribute5;
  @Bind(R.id.attribute_6) protected TextView attribute6;

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

    outerLayout.setOnClickListener(v -> onClickPlayer(v, player));
  }

  protected void onClickPlayer(View view, Player player) {
  }

  private Spannable createAttributeSpan(int attribute, String suffix) {
    SpannableString spannable = new SpannableString(String.valueOf(attribute) + " " + suffix);
    spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
    return spannable;
  }
}
