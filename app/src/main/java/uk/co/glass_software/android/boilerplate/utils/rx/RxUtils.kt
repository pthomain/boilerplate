package uk.co.glass_software.android.boilerplate.utils.rx

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