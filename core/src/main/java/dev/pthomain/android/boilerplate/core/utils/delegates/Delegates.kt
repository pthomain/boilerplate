package dev.pthomain.android.boilerplate.core.utils.delegates

import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class WeakRefDelegate<T> : ReadWriteProperty<Any, T?> {

    private var reference: WeakReference<T>? = null

    override fun getValue(thisRef: Any,
                          property: KProperty<*>) =
            reference?.get()

    override fun setValue(thisRef: Any,
                          property: KProperty<*>,
                          value: T?) {
        reference = WeakReference<T>(value)
    }
}
