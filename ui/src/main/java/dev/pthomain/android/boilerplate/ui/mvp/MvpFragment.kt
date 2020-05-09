package dev.pthomain.android.boilerplate.ui.mvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import dev.pthomain.android.boilerplate.core.HasContext
import dev.pthomain.android.boilerplate.ui.mvp.base.MvpContract

abstract class MvpFragment<
        V : MvpContract.MvpView<V, P, C>,
        P : MvpContract.Presenter<V, P, C>,
        C : MvpContract.ViewComponent<V, P, C>>
    : Fragment(), MvpContract.MvpView<V, P, C>, HasContext {

    private lateinit var component: C

    final override fun onComponentReady(component: C) {
        this.component = component
    }

    final override fun getPresenter(): P = component.presenter()

    //Final: override onCreateMvpView() instead
    final override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    //Final: override onCreateMvpView() instead
    final override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
    }

    //Final: override onCreateMvpView() instead
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent(savedInstanceState)
    }

    override fun context() = requireContext()

}