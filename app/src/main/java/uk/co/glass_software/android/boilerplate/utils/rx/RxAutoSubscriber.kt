package uk.co.glass_software.android.boilerplate.utils.rx

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface RxAutoSubscriber {

    var subscriptions: CompositeDisposable

    fun Disposable.autoAdd(): Disposable = apply { subscriptions.add(this) }

    //Observable

    fun <T> Observable<T>.autoSubscribe() =
            subscribe().autoAdd()

    fun <T> Observable<T>.autoSubscribe(onNext: (T) -> Unit) =
            subscribe(onNext).autoAdd()

    fun <T> Observable<T>.autoSubscribe(onNext: (T) -> Unit,
                                        onError: (Throwable) -> Unit) =
            subscribe(onNext, onError).autoAdd()

    fun <T> Observable<T>.autoSubscribe(onNext: (T) -> Unit,
                                        onError: (Throwable) -> Unit,
                                        onComplete: () -> Unit) =
            subscribe(onNext, onError, onComplete).autoAdd()

    //Single

    fun <T> Single<T>.autoSubscribe() =
            subscribe().autoAdd()

    fun <T> Single<T>.autoSubscribe(onSuccess: (T) -> Unit) =
            subscribe(onSuccess).autoAdd()

    fun <T> Single<T>.autoSubscribe(onSuccess: (T) -> Unit,
                                    onError: (Throwable) -> Unit) =
            subscribe(onSuccess, onError).autoAdd()

    //Maybe

    fun <T> Maybe<T>.autoSubscribe() =
            subscribe().autoAdd()

    fun <T> Maybe<T>.autoSubscribe(onSuccess: (T) -> Unit) =
            subscribe(onSuccess).autoAdd()

    fun <T> Maybe<T>.autoSubscribe(onSuccess: (T) -> Unit,
                                   onError: (Throwable) -> Unit) =
            subscribe(onSuccess, onError).autoAdd()

    fun <T> Maybe<T>.autoSubscribe(onSuccess: (T) -> Unit,
                                   onError: (Throwable) -> Unit,
                                   onComplete: () -> Unit) =
            subscribe(onSuccess, onError, onComplete).autoAdd()

    //Completable

    fun Completable.autoSubscribe() =
            subscribe().autoAdd()

    fun Completable.autoSubscribe(onComplete: () -> Unit) =
            subscribe(onComplete).autoAdd()

    fun Completable.autoSubscribe(onComplete: () -> Unit,
                                  onError: (Throwable) -> Unit) =
            subscribe(onComplete, onError).autoAdd()

}