package com.elifut.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.elifut.models.Nation
import rx.Subscription

class CountriesRecyclerViewAdapter(private val nations: List<Nation>)
  : RecyclerView.Adapter<CountryViewHolder>() {
  val subscriptions: MutableList<Subscription> = mutableListOf()
  var selectedNation: Nation? = null

  override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
      nations[position].let {
        holder.bind(it)
        holder.selected = it == selectedNation
      }

  override fun getItemCount() =
      nations.size

  override fun getItemId(position: Int) =
      nations[position].id().toLong()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      CountryViewHolder(parent).apply {
        subscriptions.add(clickStream.subscribe({
          selectedNation = it
          selected = true
          notifyDataSetChanged()
        }))
      }

  fun unbind() =
      subscriptions.forEach { it.unsubscribe() }
}

