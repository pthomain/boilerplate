package uk.co.glass_software.android.boilerplate.core.utils.resources

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

infix fun Int.string(context: Context) = context.resources.getString(this)

infix fun Int.int(context: Context) = context.resources.getInteger(this)

infix fun Int.boolean(context: Context) = context.resources.getBoolean(this)

infix fun Int.pixel(context: Context) = context.resources.getDimensionPixelSize(this)

infix fun Int.raw(context: Context) = context.resources.openRawResource(this)


// Find optional

infix fun <T : View> View?.findOptional(@IdRes id: Int) = this?.findViewById<T>(id)

infix fun <T : View> Activity?.findOptional(@IdRes id: Int) = this?.findViewById<T>(id)

infix fun <T : View> Fragment?.findOptional(@IdRes id: Int) = this?.activity?.findViewById<T>(id)

// Find present

infix fun <T : View> View?.findPresent(@IdRes id: Int) = this!!.findViewById<T>(id)!!

infix fun <T : View> Fragment?.findPresent(@IdRes id: Int) = this!!.activity!!.findViewById<T>(id)!!

infix fun <T : View> Activity?.findPresent(@IdRes id: Int) = this!!.findViewById<T>(id)!!

// Find lazy

infix fun <V : View> View.findLazy(@IdRes resId: Int) = BaseViewDelegate.ViewDelegate<V>(resId)

infix fun <V : View> Fragment.findLazy(@IdRes resId: Int) = BaseViewDelegate.FragmentViewDelegate<V>(resId)

infix fun <V : View> Activity.findLazy(@IdRes resId: Int) = BaseViewDelegate.ActivityViewDelegate<V>(resId)

// Internal

sealed class BaseViewDelegate<V : View, T>(@IdRes private val resId: Int,
                                           private val viewContainer: (T) -> (Int) -> V)
    : ReadWriteProperty<T, V> {

    private var view: V? = null

    override fun getValue(thisRef: T,
                          property: KProperty<*>) =
            view ?: viewContainer(thisRef)(resId).also { view = it }

    override fun setValue(thisRef: T,
                          property: KProperty<*>, value: V) = Unit

    class ViewDelegate<V : View>(@IdRes resId: Int)
        : BaseViewDelegate<V, ViewGroup>(resId, { ViewContainer(it) })

    class FragmentViewDelegate<V : View>(@IdRes resId: Int)
        : BaseViewDelegate<V, Fragment>(resId, { FragmentContainer(it) })

    class ActivityViewDelegate<V : View>(@IdRes resId: Int)
        : BaseViewDelegate<V, Activity>(resId, { ActivityContainer(it) })

    private class ViewContainer<V : View>(private val view: View) : (Int) -> V {
        override fun invoke(resId: Int) = view.findPresent<V>(resId)
    }

    private class FragmentContainer<V : View>(private val fragment: Fragment) : (Int) -> V {
        override fun invoke(resId: Int) = fragment.findPresent<V>(resId)
    }

    private class ActivityContainer<V : View>(private val activity: Activity) : (Int) -> V {
        override fun invoke(resId: Int) = activity.findPresent<V>(resId)
    }
}