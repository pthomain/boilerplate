package uk.co.glass_software.android.boilerplate.ui.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.co.glass_software.android.boilerplate.HasContext
import uk.co.glass_software.android.boilerplate.HasLogger
import uk.co.glass_software.android.boilerplate.ui.mvp.base.MvpContract
import uk.co.glass_software.android.boilerplate.utils.log.Logger
import uk.co.glass_software.android.boilerplate.utils.log.SimpleLogger

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