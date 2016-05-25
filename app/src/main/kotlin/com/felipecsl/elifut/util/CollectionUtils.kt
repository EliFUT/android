package com.felipecsl.elifut.util

import rx.Observable
import java.util.*

fun <T> List<T>.shuffle(): List<T> {
  Collections.shuffle(this)
  return this
}

fun <T> List<T>.sort(comparator: Comparator<in T>): List<T> {
  Collections.sort(this, comparator)
  return this
}

fun <T> Observable<T>.toList(): List<T> {
  return toList().toBlocking().first()
}