package dev.pthomain.android.boilerplate.ui.base.activity

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity(){

    protected val disposables = CompositeDisposable()

    @CallSuper
    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

}