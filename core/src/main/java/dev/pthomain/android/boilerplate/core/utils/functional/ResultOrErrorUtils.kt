package dev.pthomain.android.boilerplate.core.utils.functional

import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable.Result
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <R> Observable<R>.mapResultOrError(onNext: (R) -> Result<R> = { it.result() },
                                       onError: (Throwable) -> ResultOrThrowable.Throwable<R> = { it.error() })
        : Observable<SuccessFailure<R>> =
        compose {
            it.map { onNext(it) as SuccessFailure<R> }
                    .onErrorReturn { onError(it) }
        }

fun <R> Single<R>.mapResultOrError(onSuccess: (R) -> Result<R> = { it.result() },
                                   onError: (Throwable) -> ResultOrThrowable.Throwable<R> = { it.error() })
        : Single<SuccessFailure<R>> =
        compose {
            it.map { onSuccess(it) as SuccessFailure<R> }
                    .onErrorReturn { onError(it) }
        }

fun Completable.mapResultOrError(onSuccess: (Unit) -> Result<Unit> = { it.result() },
                                 onError: (Throwable) -> ResultOrThrowable.Throwable<Unit> = { it.error() })
        : Single<SuccessFailure<Unit>> =
        andThen(Single.just(Unit))
                .mapResultOrError(onSuccess, onError)
