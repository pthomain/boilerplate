package dev.pthomain.android.boilerplate.ui.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import dev.pthomain.android.boilerplate.ui.base.fragment.BaseFragment
import dev.pthomain.android.boilerplate_ui.R

abstract class BaseFragmentActivity : BaseActivity() {

    companion object {
        private const val CURRENT_FRAGMENT = "current_fragment"
    }

    private val containerId = R.id.fragment_container

    protected var mainFragment: BaseFragment? = null
        private set

    protected open val transition = FragmentTransaction.TRANSIT_FRAGMENT_FADE

    protected abstract fun initialiseMainFragment(): BaseFragment

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        if (savedInstanceState == null) {
            showCurrentFragment(null)
        }
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        showCurrentFragment(savedInstanceState)
    }

    private fun showCurrentFragment(savedInstanceState: Bundle?) {
        mainFragment = savedInstanceState?.let {
            supportFragmentManager.getFragment(it, CURRENT_FRAGMENT) as BaseFragment
        }

        if (mainFragment == null) {
            replaceMainFragment(
                    initialiseMainFragment(),
                    false
            )
        } else onMainFragmentAddedInternal(mainFragment!!)
    }

    @CallSuper
    protected fun replaceMainFragment(newFragment: BaseFragment,
                                      animate: Boolean) {
        if (newFragment.isAdded || mainFragment == newFragment) {
            onMainFragmentAddedInternal(newFragment)
            return
        }

        with(supportFragmentManager.beginTransaction()) {
            if (animate) setTransition(transition)
            replace(containerId, newFragment)

            if (newFragment is BackStackable) {
                addToBackStack(newFragment.getBackStackName())
                commit()
            } else {
                commitNow()
                onMainFragmentAddedInternal(newFragment)
            }
        }

        mainFragment = newFragment
    }

    @CallSuper
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is BackStackable && fragment is BaseFragment) {
            fragment.observeFragmentCreated()
                    .subscribe { onMainFragmentAddedInternal(fragment) }
                    .also { disposables.add(it) }
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mainFragment != null && mainFragment!!.isAdded()) {
            supportFragmentManager.putFragment(
                    outState,
                    CURRENT_FRAGMENT,
                    mainFragment!!
            )
        }
    }

    @CallSuper
    override fun onBackPressed() {
        if (mainFragment != null && mainFragment is BackHandler) {
            if ((mainFragment as BackHandler).onBackPressed()) return
        }

        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun onMainFragmentAddedInternal(currentFragment: BaseFragment) {
        onMainFragmentAdded(currentFragment)
    }

    open fun onMainFragmentAdded(currentFragment: BaseFragment) = Unit

    interface BackStackable {
        fun getBackStackName(): String? = null
    }

    interface BackHandler {
        fun onBackPressed(): Boolean //return true if handled, false otherwise
    }

}