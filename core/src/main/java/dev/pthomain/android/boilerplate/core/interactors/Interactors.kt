package dev.pthomain.android.boilerplate.core.interactors

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface ObservableInteractor<T> {
    fun call(): Observable<T>
}

interface SingleInteractor<T> {
    fun call(): Single<T>
}

interface FlowableInteractor<T> {
    fun call(): Flowable<T>
}

interface CompletableInteractor {
    fun call(): Completable
}