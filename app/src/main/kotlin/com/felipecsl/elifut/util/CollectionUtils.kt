package com.felipecsl.elifut.util

import com.google.common.collect.FluentIterable
import org.apache.commons.math3.random.RandomGenerator
import rx.Observable
import java.util.*

/** Moves every element of the list to a random new position in the list. */
fun <T> List<T>.shuffle() = apply { Collections.shuffle(this) }

/** Sorts this list using the provided comparator */
fun <T> List<T>.sort(comparator: Comparator<in T>) =
    FluentIterable.from(this).toSortedList(comparator)

fun <T> Observable<T>.toList() = toList().toBlocking().first()

/** Multiplies this list t times, eg.: [1, 2] times 2 = [1, 2, 1, 2] */
fun <T> List<T>.times(t: Int) = 0.rangeTo(t - 1).flatMap { this }

/** Returns a random item out of the list */
fun <T> List<T>.sample(random: RandomGenerator) = get(random.nextInt(size))