package uk.co.glass_software.android.boilerplate.utils.rx

import android.net.NetworkInfo
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.WalledGardenInternetObservingStrategy
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.glass_software.android.boilerplate.Boilerplate
import java.io.IOException
import java.util.concurrent.TimeoutException


fun <T> Observable<T>.waitForNetwork(
        boilerplate: Boilerplate,
        maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS
) = compose {
    boilerplate.waitForNetworkAndRetry(
            0,
            maxAttempts,
            it
    )
}!!

fun <T> Single<T>.waitForNetwork(
        boilerplate: Boilerplate,
        maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS
) = compose {
    boilerplate.waitForNetworkAndRetry(
            0,
            maxAttempts,
            it.toObservable()
    ).firstOrError()
}!!

fun <T> Maybe<T>.waitForNetwork(
        boilerplate: Boilerplate,
        maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS
) = compose {
    boilerplate.waitForNetworkAndRetry(
            0,
            maxAttempts,
            it.toObservable()
    ).firstElement()
}!!

fun Completable.waitForNetwork(
        boilerplate: Boilerplate,
        maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS
) = compose {
    boilerplate.waitForNetworkAndRetry(
            0,
            maxAttempts,
            it.toObservable<RxIgnore>()
    ).ignoreElements()
}!!

fun Boilerplate.getNetworkAvailableSingle(checkConnectivity: Boolean = true): Single<RxIgnore> {
    val networkSingle = networkConnectivityObservable()
            .doOnNext { if (it.state() != NetworkInfo.State.CONNECTED) logger.d(TAG, "Waiting for network...") }
            .filter { it.state() == NetworkInfo.State.CONNECTED }
            .firstOrError()

    return if (checkConnectivity) {
        networkSingle.flatMap { _ ->
            ReactiveNetwork
                    .observeInternetConnectivity(internetObservingStrategy)
                    .doOnNext { if (!it) logger.d(TAG, "Waiting for connectivity...") }
                    .filter { it }
                    .firstOrError()
        }
    } else {
        networkSingle
    }.mapIgnore().doOnSuccess { logger.d(TAG, "Network is back") }
}

fun Boilerplate.observeNetworkAvailability(checkConnectivity: Boolean = true) =
        ReactiveNetwork.observeNetworkConnectivity(context)
                .map { it.state() == NetworkInfo.State.CONNECTED }
                .compose { upstream ->
                    if (checkConnectivity)
                        upstream
                                .filter { it }
                                .flatMap { ReactiveNetwork.observeInternetConnectivity(internetObservingStrategy) }
                    else upstream
                }

private fun <T> Boilerplate.waitForNetworkAndRetry(attempt: Int,
                                                   maxAttempts: Int,
                                                   upstream: Observable<T>,
                                                   skipCheckAtStart: Boolean = false) =
        upstream.onErrorResumeNext { error: Throwable ->
            resumeNetworkOnError(
                    attempt + 1,
                    maxAttempts,
                    error,
                    upstream
            )
        }.let { composed ->
            if (skipCheckAtStart && attempt == 0) composed //useful for cache to work when offline
            else getNetworkAvailableSingle().flatMapObservable { composed }
        }

private fun Boilerplate.networkConnectivityObservable() =
        ReactiveNetwork.observeNetworkConnectivity(context)

private fun isNetworkError(error: Throwable) =
        error is TimeoutException || error is IOException

private fun <T> Boilerplate.resumeNetworkOnError(attempt: Int,
                                                 maxAttempts: Int,
                                                 error: Throwable,
                                                 upstream: Observable<T>): Observable<T> {
    return if (attempt >= maxAttempts) {
        logger.e(TAG, "Could not establish network connection after $attempt attempts")
        Observable.error<T>(TooManyAttemptsException(maxAttempts, error))
    } else {
        if (isNetworkError(error)) {
            logger.d(TAG, "Lost network connection, waiting for reconnection: attempt $attempt")
            waitForNetworkAndRetry(
                    attempt + 1,
                    maxAttempts,
                    upstream
            )
        } else {
            logger.e(TAG, error, "Caught a non-network error")
            Observable.error<T>(error)
        }
    }
}

private val internetObservingStrategy = InternetObservingSettings
        .builder()
        .strategy(object : WalledGardenInternetObservingStrategy() {
            //Android P enforces TLS/SSL for all requests
            override fun getDefaultPingHost() = "https://clients3.google.com/generate_204"
        })
        .build()

class TooManyAttemptsException internal constructor(
        maxAttempts: Int,
        error: Throwable
) : Exception(
        "Too many attempts to reconnect to the network (max $maxAttempts attempts)",
        error
)