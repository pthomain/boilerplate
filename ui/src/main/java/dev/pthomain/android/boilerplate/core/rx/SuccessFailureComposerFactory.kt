package dev.pthomain.android.boilerplate.core.rx

import dev.pthomain.android.boilerplate.core.utils.functional.SuccessFailure
import dev.pthomain.android.boilerplate.core.utils.functional.mapResultOrError
import dev.pthomain.android.boilerplate.core.utils.rx.RxTransformer
import io.reactivex.Observable

interface SuccessFailureComposerFactory {

    fun <R> newComposer(
            onSubscribe: () -> Unit = {},
            onTerminate: () -> Unit = {}
    ): RxTransformer<R, SuccessFailure<R>>

}

class SimpleSuccessFailureComposerFactory : SuccessFailureComposerFactory {

    override fun <R> newComposer(onSubscribe: () -> Unit,
                                 onTerminate: () -> Unit) =
            SuccessFailureTransformer<R>(
                    onSubscribe,
                    onTerminate
            )

}

class SuccessFailureTransformer<R>(
        private val onSubscribe: () -> Unit,
        private val onTerminate: () -> Unit
) : RxTransformer<R, SuccessFailure<R>>() {

    override fun apply(upstream: Observable<R>) =
            upstream.mapResultOrError()
                    .doOnSubscribe { onSubscribe() }
                    .doFinally { onTerminate() }

}
