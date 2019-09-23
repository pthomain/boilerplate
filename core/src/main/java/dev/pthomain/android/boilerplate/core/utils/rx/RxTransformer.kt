package dev.pthomain.android.boilerplate.core.utils.rx

import io.reactivex.*

abstract class RxTransformer<I, O>
    : ObservableTransformer<I, O>,
        SingleTransformer<I, O>,
        CompletableTransformer {

    final override fun apply(upstream: Single<I>) =
            upstream.toObservable()
                    .compose(this)
                    .firstOrError()!!

    @Suppress("UNCHECKED_CAST")
    final override fun apply(upstream: Completable) =
            upstream.andThen(Observable.just(Any()))
                    .compose(this as ObservableTransformer<in Any, out Any>)
                    .ignoreElements()!!

}

abstract class SimpleRxTransformer<T> : RxTransformer<T, T>()