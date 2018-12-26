package uk.co.glass_software.android.boilerplate.utils.rx

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

object RxIgnore {
    fun observable() = Observable.just(this)
    fun single() = Single.just(this)
    fun maybe() = Maybe.just(this)
}

fun <T> Observable<T>.mapIgnore() = map { RxIgnore }
fun <T> Single<T>.mapIgnore() = map { RxIgnore }
fun <T> Maybe<T>.mapIgnore() = map { RxIgnore }

abstract class Ignorable : Observable<RxIgnore>()
abstract class SingleIgnorable : Single<RxIgnore>()
abstract class MaybeIgnorable : Maybe<RxIgnore>()