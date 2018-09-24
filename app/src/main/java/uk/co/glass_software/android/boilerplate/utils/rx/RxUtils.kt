package uk.co.glass_software.android.boilerplate.utils.rx

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor


fun <T> Observable<T>.io() = schedule(On.Io)
fun <T> Single<T>.io() = schedule(On.Io)
fun <T> Maybe<T>.io() = schedule(On.Io)
fun Completable.io() = schedule(On.Io)

fun <T> Observable<T>.computation() = schedule(On.Computation)
fun <T> Single<T>.computation() = schedule(On.Computation)
fun <T> Maybe<T>.computation() = schedule(On.Computation)
fun Completable.computation() = schedule(On.Computation)

fun <T> Observable<T>.schedule(subscribeOn: On,
                               observeOn: On = On.MainThread) = compose {
    it.subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

fun <T> Single<T>.schedule(subscribeOn: On,
                           observeOn: On = On.MainThread) = compose {
    it.subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

fun <T> Maybe<T>.schedule(subscribeOn: On,
                          observeOn: On = On.MainThread) = compose {
    it.subscribeOn(subscribeOn.instance)
            .observeOn(observeOn.instance)
}!!

fun Completable.schedule(subscribeOn: On,
                         observeOn: On = On.MainThread) = compose {
    it.subscribeOn(subscribeOn.instance)
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