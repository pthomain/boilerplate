package uk.co.glass_software.android.boilerplate.utils.rx


import io.reactivex.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.glass_software.android.boilerplate.Boilerplate
import uk.co.glass_software.android.boilerplate.utils.log.log
import uk.co.glass_software.android.boilerplate.utils.rx.On.*
import java.util.concurrent.Executor

const val TAG = "RxScheduling"
const val MAX_NETWORK_RECONNECT_ATTEMPTS = 3

fun <T> Observable<T>.ioUi() =
        subscribeOnIo(MainThread)

fun <T> Single<T>.ioUi() =
        subscribeOnIo(MainThread)

fun <T> Maybe<T>.ioUi() =
        subscribeOnIo(MainThread)

fun <T> Flowable<T>.ioUi() =
        subscribeOnIo(MainThread)

fun Completable.ioUi() =
        subscribeOnIo(MainThread)

fun <T> Observable<T>.subscribeOnIo(observeOn: On = Trampoline) =
        schedule(Io, observeOn)

fun <T> Single<T>.subscribeOnIo(observeOn: On = Trampoline) =
        schedule(Io, observeOn)

fun <T> Maybe<T>.subscribeOnIo(observeOn: On = Trampoline) =
        schedule(Io, observeOn)

fun <T> Flowable<T>.subscribeOnIo(observeOn: On = Trampoline) =
        schedule(Io, observeOn)

fun Completable.subscribeOnIo(observeOn: On = Trampoline) =
        schedule(Io, observeOn)

fun <T> Observable<T>.schedule(subscribeOn: On = Io,
                               observeOn: On = MainThread,
                               boilerplate: Boilerplate? = null) = compose {
    it.doOnError { boilerplate?.log(TAG, it) }
            .subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

fun <T> Single<T>.schedule(subscribeOn: On = Io,
                           observeOn: On = MainThread,
                           boilerplate: Boilerplate? = null) = compose {
    it.doOnError { boilerplate?.log(TAG, it) }
            .subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

fun <T> Maybe<T>.schedule(subscribeOn: On = Io,
                          observeOn: On = MainThread,
                          boilerplate: Boilerplate? = null) = compose {
    it.doOnError { boilerplate?.log(TAG, it) }
            .subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

fun <T> Flowable<T>.schedule(subscribeOn: On = Io,
                             observeOn: On = MainThread,
                             boilerplate: Boilerplate? = null) = compose {
    it.doOnError { boilerplate?.log(TAG, it) }
            .subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

fun Completable.schedule(subscribeOn: On = Io,
                         observeOn: On = MainThread,
                         boilerplate: Boilerplate? = null) = compose {
    it.doOnError { boilerplate?.log(TAG, it) }
            .subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

sealed class On(val instance: Scheduler) {

    object Computation : On(Schedulers.computation())
    object Io : On(Schedulers.io())
    object Trampoline : On(Schedulers.trampoline())
    object NewThread : On(Schedulers.newThread())
    object Single : On(Schedulers.single())
    object MainThread : On(AndroidSchedulers.mainThread())

    class From(executor: Executor) : On(Schedulers.from(executor))

}