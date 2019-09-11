package uk.co.glass_software.android.boilerplate.core.rx

import io.reactivex.Observable
import uk.co.glass_software.android.boilerplate.core.utils.functional.SuccessFailure
import uk.co.glass_software.android.boilerplate.core.utils.functional.mapResultOrError
import uk.co.glass_software.android.boilerplate.core.utils.rx.RxTransformer

interface SuccessFailureComposerFactory {

    fun <R> newComposer(
            onSubscribe: () -> Unit = {},
            onTerminate: () -> Unit = {}
    ): RxTransformer<R, SuccessFailure<R>>

}

class SimpleSuccessFailureComposerFactory : SuccessFailureComposerFactory {

    override fun <R> newComposer(onSubscribe: () -> Unit,
                                 onTerminate: () -> Unit) =
            SuccessFailureTransformer<R>()

}

class SuccessFailureTransformer<R> : RxTransformer<R, SuccessFailure<R>>() {

    override fun apply(upstream: Observable<R>) =
            upstream.mapResultOrError()

}
