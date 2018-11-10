package uk.co.glass_software.android.boilerplate.utils.rx


import android.net.NetworkInfo.State.CONNECTED
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.glass_software.android.boilerplate.Boilerplate
import uk.co.glass_software.android.boilerplate.Boilerplate.logger
import uk.co.glass_software.android.boilerplate.utils.rx.On.*
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.TimeoutException

private val context = Boilerplate.context
private val logger = Boilerplate.logger
private val networkConnectivityObservable = ReactiveNetwork.observeNetworkConnectivity(context)

const val MAX_NETWORK_RECONNECT_ATTEMPTS = 3

fun <T> Observable<T>.ioUi(waitForNetwork: Boolean = true) =
        subscribeOnIo(waitForNetwork, MainThread)

fun <T> Single<T>.ioUi(waitForNetwork: Boolean = true) =
        subscribeOnIo(waitForNetwork, MainThread)

fun <T> Maybe<T>.ioUi(waitForNetwork: Boolean = true) =
        subscribeOnIo(waitForNetwork, MainThread)

fun Completable.ioUi(waitForNetwork: Boolean = true) =
        subscribeOnIo(waitForNetwork, MainThread)

fun <T> Observable<T>.subscribeOnIo(waitForNetwork: Boolean = true,
                                    observeOn: On = Trampoline) =
        schedule(Io, observeOn, waitForNetwork)

fun <T> Single<T>.subscribeOnIo(waitForNetwork: Boolean = true,
                                observeOn: On = Trampoline) =
        schedule(Io, observeOn, waitForNetwork)

fun <T> Maybe<T>.subscribeOnIo(waitForNetwork: Boolean = true,
                               observeOn: On = Trampoline) =
        schedule(Io, observeOn, waitForNetwork)

fun Completable.subscribeOnIo(waitForNetwork: Boolean = false,
                              observeOn: On = Trampoline) = schedule(Io, observeOn, waitForNetwork)

fun <T> Observable<T>.schedule(subscribeOn: On = Io,
                               observeOn: On = MainThread,
                               waitForNetwork: Boolean = subscribeOn == Io,
                               maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    (if (waitForNetwork) it.waitForNetwork(maxAttempts) else it)
            .doOnError { logger.e(it) }
            .subscribeOn(subscribeOn.instance())
            .observeOn(observeOn.instance())
}!!

fun <T> Single<T>.schedule(subscribeOn: On = Io,
                           observeOn: On = MainThread,
                           waitForNetwork: Boolean = subscribeOn == Io,
                           maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    (if (waitForNetwork) it.waitForNetwork(maxAttempts) else it)
            .doOnError { logger.e(it) }
            .subscribeOn(subscribeOn.instance())
            .observeOn(observeOn.instance())
}!!

fun <T> Maybe<T>.schedule(subscribeOn: On = Io,
                          observeOn: On = MainThread,
                          waitForNetwork: Boolean = subscribeOn == Io,
                          maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    (if (waitForNetwork) it.waitForNetwork(maxAttempts) else it)
            .doOnError { logger.e(it) }
            .subscribeOn(subscribeOn.instance())
            .observeOn(observeOn.instance())
}!!

fun Completable.schedule(subscribeOn: On = Io,
                         observeOn: On = MainThread,
                         waitForNetwork: Boolean = subscribeOn == Io,
                         maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    (if (waitForNetwork) it.waitForNetwork(maxAttempts) else it)
            .doOnError { logger.e(it) }
            .subscribeOn(subscribeOn.instance())
            .observeOn(observeOn.instance())
}!!

fun <T> Observable<T>.waitForNetwork(maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    waitForNetworkAndRetry(0, maxAttempts, it)
}!!

fun <T> Single<T>.waitForNetwork(maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    waitForNetworkAndRetry(0, maxAttempts, it.toObservable()).firstOrError()
}!!

fun <T> Maybe<T>.waitForNetwork(maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    waitForNetworkAndRetry(0, maxAttempts, it.toObservable()).firstElement()
}!!

fun Completable.waitForNetwork(maxAttempts: Int = MAX_NETWORK_RECONNECT_ATTEMPTS) = compose {
    waitForNetworkAndRetry(0, maxAttempts, it.toObservable<RxIgnore>()).ignoreElements()
}!!

fun getNetworkAvailableSingle(checkConnectivity: Boolean = true): Single<RxIgnore> {
    val networkSingle = networkConnectivityObservable
            .doOnNext { if (it.state() != CONNECTED) log("Waiting for network...") }
            .filter { it.state() == CONNECTED }
            .firstOrError()

    return if (checkConnectivity) {
        networkSingle.flatMap { _ ->
            ReactiveNetwork
                    .observeInternetConnectivity()
                    .doOnNext { if (!it) log("Waiting for connectivity...") }
                    .filter { it }
                    .firstOrError()
        }
    } else {
        networkSingle
    }.mapIgnore().doOnSuccess { log("Network is back") }
}

private fun <T> waitForNetworkAndRetry(attempt: Int,
                                       maxAttempts: Int,
                                       upstream: Observable<T>,
                                       skipCheckAtStart: Boolean = false): Observable<T> {
    val composed = upstream.onErrorResumeNext { error: Throwable ->
        resumeNetworkOnError(
                attempt + 1,
                maxAttempts,
                error,
                upstream
        )
    }

    return if (skipCheckAtStart && attempt == 0) composed //useful for cache to work when offline
    else getNetworkAvailableSingle().flatMapObservable { composed }
}

private fun isNetworkError(error: Throwable) =
        error is TimeoutException || error is IOException

private fun <T> resumeNetworkOnError(attempt: Int,
                                     maxAttempts: Int,
                                     error: Throwable,
                                     upstream: Observable<T>): Observable<T> {
    return if (attempt >= maxAttempts) {
        logger.e("Could not establish network connection after $attempt attempts")
        Observable.error<T>(TooManyAttemptsException(maxAttempts, error))
    } else {
        if (isNetworkError(error)) {
            log("Lost network connection, waiting for reconnection: attempt $attempt")
            waitForNetworkAndRetry(
                    attempt + 1,
                    maxAttempts,
                    upstream
            )
        } else {
            logger.e(error, "Caught a non-network error")
            Observable.error<T>(error)
        }
    }
}

class TooManyAttemptsException(maxAttempts: Int,
                               error: Throwable)
    : Exception(
        "Too many attempts to reconnect to the network (max $maxAttempts attempts)",
        error
)

private fun log(message: String) {
    logger.d("Network", message)
}

sealed class On(val instance: () -> Scheduler) {

    object Computation : On({ Schedulers.computation() })
    object Io : On({ Schedulers.io() })
    object Trampoline : On({ Schedulers.trampoline() })
    object NewThread : On({ Schedulers.newThread() })
    object Single : On({ Schedulers.single() })
    object MainThread : On({ AndroidSchedulers.mainThread() })

    class From(executor: Executor) : On({ Schedulers.from(executor) })

}
