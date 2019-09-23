package dev.pthomain.android.boilerplate.core.utils.optional

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Observable<Optional<T>>.mapOptional(defaultValue: T? = null): Observable<T> =
        mapOptional(defaultValue?.let { { it } })

fun <T> Single<Optional<T>>.mapOptional(defaultValue: T? = null): Single<T> =
        mapOptional(defaultValue?.let { { it } })

fun <T> Maybe<Optional<T>>.mapOptional(defaultValue: T? = null): Maybe<T> =
        mapOptional(defaultValue?.let { { it } })

fun <T> Observable<Optional<T>>.mapOptional(defaultValueSupplier: (() -> T)? = null): Observable<T> = flatMap {
    decapsulateOptional<T, Observable<T>>(
            it,
            defaultValueSupplier,
            { Observable.just(it) },
            { Observable.empty() }
    )
}

fun <T> Single<Optional<T>>.mapOptional(defaultValueSupplier: (() -> T)? = null): Single<T> = flatMap {
    decapsulateOptional<T, Single<T>>(
            it,
            defaultValueSupplier,
            { Single.just(it) },
            { Single.error(NoSuchElementException("Optional was empty and no default value was provided")) }
    )
}

fun <T> Maybe<Optional<T>>.mapOptional(defaultValueSupplier: (() -> T)? = null): Maybe<T> = flatMap {
    decapsulateOptional<T, Maybe<T>>(
            it,
            defaultValueSupplier,
            { Maybe.just(it) },
            { Maybe.empty() }
    )
}

private fun <T, RX> decapsulateOptional(optional: Optional<T>,
                                        defaultValueSupplier: (() -> T)? = null,
                                        presentRxType: (T) -> RX,
                                        absentRxType: () -> RX): RX {
    val value = optional.get() ?: defaultValueSupplier?.let { optional.orElseGet(it) }
    return value?.let { presentRxType(it) } ?: absentRxType()
}

inline fun <reified T> Optional<T>.singleOrError() = Single.defer {
    Single.just(orElseThrow { NoSuchElementException("No value found") })
}