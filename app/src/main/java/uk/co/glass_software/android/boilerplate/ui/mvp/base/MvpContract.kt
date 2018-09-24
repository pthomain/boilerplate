package uk.co.glass_software.android.boilerplate.ui.mvp.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner


interface MvpContract {

    interface Presenter<P : Presenter<P, V>, V : View<V, P>> : LifecycleObserver {
        fun getView(): V
    }

    interface View<V : View<V, P>, P : Presenter<P, V>> : LifecycleOwner {
        fun getPresenter(): P
    }
}
