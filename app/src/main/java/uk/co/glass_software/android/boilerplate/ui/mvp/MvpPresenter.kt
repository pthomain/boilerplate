package uk.co.glass_software.android.boilerplate.ui.mvp

import uk.co.glass_software.android.boilerplate.ui.mvp.base.MvpContract

abstract class MvpPresenter<P : MvpContract.Presenter<P, V>, V : MvpContract.View<V, P>>(private val v: V)
    : MvpContract.Presenter<P, V> {

    final override fun getView(): V = v

}
