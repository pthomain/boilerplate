package uk.co.glass_software.android.boilerplate.ui.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.co.glass_software.android.boilerplate.ui.mvp.base.MvpComponent
import uk.co.glass_software.android.boilerplate.ui.mvp.base.MvpContract
import javax.inject.Inject

abstract class MvpActivity<V : MvpContract.View<V, P>, P : MvpContract.Presenter<P, V>, C : MvpComponent<V, P>>
    : AppCompatActivity(), MvpContract.View<V, P> {

    @Inject
    lateinit var p: P

    final override fun getPresenter(): P = p
    abstract fun getComponent(): C
    abstract fun getMvpView(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = getComponent().presenter
        lifecycle.addObserver(p)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(p)
    }

}