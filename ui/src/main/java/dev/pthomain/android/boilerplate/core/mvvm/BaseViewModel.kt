package dev.pthomain.android.boilerplate.core.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import dev.pthomain.android.boilerplate.core.rx.SimpleSuccessFailureComposerFactory
import dev.pthomain.android.boilerplate.core.rx.SuccessFailureComposerFactory
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
        protected val logger: Logger,
        private val disposables: CompositeDisposable = CompositeDisposable(),
        composerFactory: SuccessFailureComposerFactory = SimpleSuccessFailureComposerFactory()
) : ViewModel(), RxCallHandler by ViewModelRxCallHandler(disposables, logger, composerFactory) {

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}

