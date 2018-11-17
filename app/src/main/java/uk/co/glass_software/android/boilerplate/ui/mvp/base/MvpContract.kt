package uk.co.glass_software.android.boilerplate.ui.mvp.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import uk.co.glass_software.android.boilerplate.utils.rx.RxAutoSubscriber

interface MvpContract {

    interface Presenter<
            V : MvpView<V, P, C>,
            P : Presenter<V, P, C>,
            C : ViewComponent<V, P, C>> : LifecycleObserver, RxAutoSubscriber {

        val mvpView: V

    }

    interface MvpView<
            V : MvpView<V, P, C>,
            P : Presenter<V, P, C>,
            C : ViewComponent<V, P, C>> : LifecycleOwner {

        @Suppress("UNCHECKED_CAST")
        fun getMvpView(): V = this as V

        fun getPresenter(): P
        fun initialiseComponent(): C
        fun onComponentReady(component: C)

        @CallSuper
        fun onCreateComponent(savedInstanceState: Bundle?) {
            onCreateMvpView(savedInstanceState)
            onComponentReady(initialiseComponent())
            lifecycle.addObserver(getPresenter())
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyMvpView() {
                    lifecycle.removeObserver(getPresenter())
                }
            })
            onMvpViewCreated()
        }

        //If needed do some initialisation here using Bundle
        fun onCreateMvpView(savedInstanceState: Bundle?) = Unit
        fun onMvpViewCreated() = Unit
    }

    interface ViewComponent<
            V : MvpContract.MvpView<V, P, C>,
            P : MvpContract.Presenter<V, P, C>,
            C : ViewComponent<V, P, C>> {
        fun presenter(): P
    }

}