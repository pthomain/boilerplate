package uk.co.glass_software.android.boilerplate.ui.mvp.base

interface MvpComponent<V : MvpContract.View<V, P>, P : MvpContract.Presenter<P, V>> {

    val presenter: P

}
