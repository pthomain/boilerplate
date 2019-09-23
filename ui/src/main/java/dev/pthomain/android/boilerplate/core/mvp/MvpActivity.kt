package dev.pthomain.android.boilerplate.core.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.pthomain.android.boilerplate.core.HasContext
import dev.pthomain.android.boilerplate.core.HasLogger
import dev.pthomain.android.boilerplate.core.mvp.base.MvpContract

abstract class MvpActivity<
        V : MvpContract.MvpView<V, P, C>,
        P : MvpContract.Presenter<V, P, C>,
        C : MvpContract.ViewComponent<V, P, C>>
    : AppCompatActivity(), MvpContract.MvpView<V, P, C>, HasContext, HasLogger {

    private lateinit var component: C

    final override fun getPresenter(): P = component.presenter()

    final override fun onComponentReady(component: C) {
        this.component = component
    }

    //Final: override onCreateMvpView() instead
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent(savedInstanceState)
    }

    override fun onMvpViewCreated() = Unit

    override fun context() = applicationContext

}