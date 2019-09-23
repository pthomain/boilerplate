package dev.pthomain.android.boilerplate.core.utils.functional

import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable.Result

sealed class ResultOrError<R, E, T>(
        private val result: R?,
        private val error: E?
) {

    abstract val value: T

    sealed class ResultOrThrowable<R, T>(
            result: R?,
            error: kotlin.Throwable?
    ) : ResultOrError<R, Throwable, T>(result, error) {

        class Result<R>(result: R) : ResultOrThrowable<R, R>(result, null) {
            override val value = result
        }

        class Throwable<R>(throwable: kotlin.Throwable) : ResultOrThrowable<R, kotlin.Throwable>(null, throwable) {
            override val value = throwable
        }
    }
}

typealias SuccessFailure<R> = ResultOrThrowable<R, * >

fun <R> R.result(): Result<R> = Result(this)
fun <R> Throwable.error(): ResultOrThrowable.Throwable<R> = ResultOrThrowable.Throwable(this)
