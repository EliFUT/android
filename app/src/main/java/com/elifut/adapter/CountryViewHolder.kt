package com.elifut.adapter

import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.elifut.R
import com.elifut.models.Nation
import com.elifut.util.CircleTransform
import com.jakewharton.rxbinding.view.RxView
import com.squareup.picasso.Picasso
import rx.subjects.PublishSubject

class CountryViewHolder(parent: ViewGroup) :
    BaseViewHolder<Nation>(parent, R.layout.country_view_holder) {
  private val imgFlag: ImageView = itemView.findViewById(R.id.img_country)
  private val txtName: TextView = itemView.findViewById(R.id.text_country)
  private val layout: ViewGroup = itemView.findViewById(R.id.layout)
  val clickStream: PublishSubject<Nation> = PublishSubject.create<Nation>()
  var selected: Boolean = false
    set(_selected) {
      val color = if (_selected) R.color.light_gray else android.R.color.transparent
      setBackground(color)
    }

  override fun bind(nation: Nation) {
    RxView.clicks(layout)
        .map { nation }
        .subscribe {
          clickStream.onNext(it)
        }
    txtName.text = nation.toString()
    Picasso.with(itemView.context)
        .load(nation.large_image())
        .transform(CircleTransform())
        .into(imgFlag)
  }

  private fun setBackground(color: Int) =
      layout.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
}