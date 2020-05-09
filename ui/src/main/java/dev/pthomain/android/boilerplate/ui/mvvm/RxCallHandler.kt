package dev.pthomain.android.boilerplate.ui.mvvm

import androidx.lifecycle.MutableLiveData
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

interface RxCallHandler {

    fun <R> Observable<R>.autoSubscribe(
            liveData: MutableLiveData<ResultOrThrowable<R, *>>,
            onSubscribe: () -> Unit = {},
            onTerminate: () -> Unit = {}
    ): Disposable

    fun <R> Single<R>.autoSubscribe(
            liveData: MutableLiveData<ResultOrThrowable<R, *>>,
            onSubscribe: () -> Unit = {},
            onTerminate: () -> Unit = {}
    ): Disposable

    fun Completable.autoSubscribe(
            liveData: MutableLiveData<ResultOrThrowable<*, *>>,
            onSubscribe: () -> Unit = {},
            onTerminate: () -> Unit = {}
    ): Disposable

    fun observeCallLoading(): Observable<Boolean>

}