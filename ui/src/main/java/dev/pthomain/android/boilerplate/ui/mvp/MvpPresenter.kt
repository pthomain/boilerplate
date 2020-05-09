package dev.pthomain.android.boilerplate.ui.mvp

import androidx.lifecycle.LifecycleObserver
import dev.pthomain.android.boilerplate.core.HasContext
import dev.pthomain.android.boilerplate.ui.mvp.base.MvpContract
import io.reactivex.disposables.CompositeDisposable

abstract class MvpPresenter<
        V : MvpContract.MvpView<V, P, C>,
        P : MvpContract.Presenter<V, P, C>,
        C : MvpContract.ViewComponent<V, P, C>>(
        override val mvpView: V,
        override val subscriptions: CompositeDisposable = CompositeDisposable()
) : MvpContract.Presenter<V, P, C>,
        LifecycleObserver,
        HasContext by mvpView
