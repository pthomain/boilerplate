package dev.pthomain.android.boilerplate.core.utils.rx

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

fun <T> T?.observable() =
        if (this == null) Observable.error<T>(NoSuchElementException())
        else Observable.just(this)

fun <T> T?.single() =
        if (this == null) Single.error<T>(NoSuchElementException())
        else Single.just(this)

fun <T> T?.maybe() =
        if (this == null) Maybe.empty<T>()
        else Maybe.just(this)

fun <T> T?.flowable() =
        if (this == null) Flowable.error<T>(NoSuchElementException())
        else Flowable.just(this)

fun <T> Observable<T>.ignore() = map { Unit }
fun <T> Single<T>.ignore() = map { Unit }
fun <T> Maybe<T>.ignore() = map { Unit }
fun <T> Flowable<T>.ignore() = map { Unit }