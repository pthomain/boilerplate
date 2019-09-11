package uk.co.glass_software.android.boilerplate.core.mvvm

import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import uk.co.glass_software.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable

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