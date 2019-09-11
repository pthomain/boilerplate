package uk.co.glass_software.android.boilerplate.core.mvvm

import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import uk.co.glass_software.android.boilerplate.core.rx.SimpleSuccessFailureComposerFactory
import uk.co.glass_software.android.boilerplate.core.rx.SuccessFailureComposerFactory
import uk.co.glass_software.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable
import uk.co.glass_software.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable.Result
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger

class ViewModelRxCallHandler(
        private val disposables: CompositeDisposable,
        private val logger: Logger,
        private val composerFactory: SuccessFailureComposerFactory = SimpleSuccessFailureComposerFactory()
) : RxCallHandler {

    override fun <T> Observable<T>.autoSubscribe(liveData: MutableLiveData<ResultOrThrowable<T, *>>,
                                                 onSubscribe: () -> Unit,
                                                 onTerminate: () -> Unit) =
            compose(composerFactory.newComposer(onSubscribe, onTerminate))
                    .subscribe(
                            { liveData.postValue(it) },
                            { logger.e(this, it) }
                    )
                    .also { disposables.add(it) }

    override fun <T> Single<T>.autoSubscribe(liveData: MutableLiveData<ResultOrThrowable<T, *>>,
                                             onSubscribe: () -> Unit,
                                             onTerminate: () -> Unit) =
            compose(composerFactory.newComposer(onSubscribe, onTerminate))
                    .subscribe(
                            { liveData.postValue(it) },
                            { logger.e(this, it) }
                    )
                    .also { disposables.add(it) }

    override fun Completable.autoSubscribe(liveData: MutableLiveData<ResultOrThrowable<*, *>>,
                                           onSubscribe: () -> Unit,
                                           onTerminate: () -> Unit) =
            compose(composerFactory.newComposer<Unit>(onSubscribe, onTerminate))
                    .subscribe(
                            { liveData.postValue(Result(Unit)) },
                            { logger.e(this, it) }
                    )
                    .also { disposables.add(it) }

}

