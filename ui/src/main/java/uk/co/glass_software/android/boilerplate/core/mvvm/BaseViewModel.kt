package uk.co.glass_software.android.boilerplate.core.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import uk.co.glass_software.android.boilerplate.core.rx.SimpleSuccessFailureComposerFactory
import uk.co.glass_software.android.boilerplate.core.rx.SuccessFailureComposerFactory
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger

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

