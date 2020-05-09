package dev.pthomain.android.boilerplate.ui.mvvm

import androidx.lifecycle.MutableLiveData
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable.Result
import dev.pthomain.android.boilerplate.core.utils.functional.SuccessFailure
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import dev.pthomain.android.boilerplate.core.utils.rx.RxTransformer
import dev.pthomain.android.boilerplate.ui.rx.SimpleSuccessFailureComposerFactory
import dev.pthomain.android.boilerplate.ui.rx.SuccessFailureComposerFactory
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class ViewModelRxCallHandler(
        private val disposables: CompositeDisposable,
        private val logger: Logger,
        private val composerFactory: SuccessFailureComposerFactory = SimpleSuccessFailureComposerFactory()
) : RxCallHandler {

    private val loadingSubject: Subject<Boolean> = BehaviorSubject.create<Boolean>().apply {
        onNext(false)
    }

    override fun observeCallLoading(): Observable<Boolean> = loadingSubject.map { it } //read-only

    override fun <T> Observable<T>.autoSubscribe(liveData: MutableLiveData<ResultOrThrowable<T, *>>,
                                                 onSubscribe: () -> Unit,
                                                 onTerminate: () -> Unit) =
            compose(newComposer<T>(onSubscribe, onTerminate))
                    .subscribe(
                            { liveData.postValue(it) },
                            { logger.e(this, it) }
                    )
                    .also { disposables.add(it) }

    override fun <T> Single<T>.autoSubscribe(liveData: MutableLiveData<ResultOrThrowable<T, *>>,
                                             onSubscribe: () -> Unit,
                                             onTerminate: () -> Unit) =
            compose(newComposer<T>(onSubscribe, onTerminate))
                    .subscribe(
                            { liveData.postValue(it) },
                            { logger.e(this, it) }
                    )
                    .also { disposables.add(it) }

    override fun Completable.autoSubscribe(liveData: MutableLiveData<ResultOrThrowable<*, *>>,
                                           onSubscribe: () -> Unit,
                                           onTerminate: () -> Unit) =
            compose(newComposer<Unit>(onSubscribe, onTerminate))
                    .subscribe(
                            { liveData.postValue(Result(Unit)) },
                            { logger.e(this, it) }
                    )
                    .also { disposables.add(it) }

    private fun <T> newComposer(onSubscribe: () -> Unit,
                                onTerminate: () -> Unit): RxTransformer<T, SuccessFailure<T>> =
            composerFactory.newComposer(
                    {
                        loadingSubject.onNext(true)
                        onSubscribe()
                    },
                    {
                        loadingSubject.onNext(false)
                        onTerminate()
                    }
            )

}

