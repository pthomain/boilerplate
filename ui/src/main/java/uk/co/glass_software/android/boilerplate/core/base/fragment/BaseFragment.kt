package uk.co.glass_software.android.boilerplate.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BaseFragment : Fragment() {

    @LayoutRes
    abstract fun layout(): Int

    private val creationSubject = PublishSubject.create<Unit>()

    protected val disposables = CompositeDisposable()

    final override fun onCreateView(inflater: LayoutInflater,
                                    container: ViewGroup?,
                                    savedInstanceState: Bundle?) =
            inflater.inflate(
                    layout(),
                    container,
                    false
            )?.also { onLayoutInflated(it) }

    abstract fun onLayoutInflated(layout: View)

    @CallSuper
    override fun onResume() {
        super.onResume()
        creationSubject.onNext(Unit)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    fun observeFragmentCreated() = creationSubject.map { it }!!

}