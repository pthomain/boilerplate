package uk.co.glass_software.android.boilerplate

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers


fun <T> Observable<T>.io() = compose {
    it.subscribeOn(Schedulers.io())
            .subscribeOn(mainThread())
}

fun <T> Single<T>.io() = compose {
    it.subscribeOn(Schedulers.io())
            .subscribeOn(mainThread())
}

fun <T> Maybe<T>.io() = compose {
    it.subscribeOn(Schedulers.io())
            .subscribeOn(mainThread())
}

fun Completable.io() = compose {
    it.subscribeOn(Schedulers.io())
            .subscribeOn(mainThread())
}

