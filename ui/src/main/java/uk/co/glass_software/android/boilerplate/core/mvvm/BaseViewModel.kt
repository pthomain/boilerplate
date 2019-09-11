package uk.co.glass_software.android.boilerplate.core.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger

abstract class BaseViewModel(
        logger: Logger,
        private val disposables: CompositeDisposable = CompositeDisposable()
) : ViewModel(), RxCallHandler by ViewModelRxCallHandler(disposables, logger) {

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}

