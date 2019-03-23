package uk.co.glass_software.android.boilerplate.utils.delegates

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import uk.co.glass_software.android.boilerplate.Boilerplate
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@SuppressLint("ApplySharedPref") //Apply is not thread-safe
class SharedPrefsDelegate<T>(private val prefs: Prefs,
                             private val valueKey: String)
    : ReadWriteProperty<Any, T?> {

    companion object {
        fun getPrefs(prefsFile: String) = Prefs.with(prefsFile)

        inline infix fun <reified T> with(valueKey: String): SharedPrefsDelegate<T> =
                with(valueKey, valueKey)

        inline fun <reified T> with(prefsFile: String,
                                    valueKey: String): SharedPrefsDelegate<T> =
                with(getPrefs(prefsFile), valueKey)

        inline fun <reified T> with(prefs: Prefs,
                                    valueKey: String): SharedPrefsDelegate<T> =
                SharedPrefsDelegate(prefs, valueKey)

        fun clear(valueKey: String) {
            clear(getPrefs(valueKey), valueKey)
        }

        fun clear(prefs: Prefs,
                  valueKey: String) {
            prefs.file.edit().remove(valueKey).commit()
        }

    }

    @Synchronized
    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: Any,
                                   property: KProperty<*>): T? =
            prefs.file.all[valueKey]  as? T?

    @Synchronized
    @SuppressLint("CommitPrefEdits")
    override operator fun setValue(thisRef: Any,
                                   property: KProperty<*>, value: T?) {
        prefs.file.edit().apply {
            when (value) {
                is String -> putString(valueKey, value)
                is Float -> putFloat(valueKey, value)
                is Long -> putLong(valueKey, value)
                is Boolean -> putBoolean(valueKey, value)
                is Int -> putInt(valueKey, value)
                else -> return
            }
            commit()
        }
    }

    fun clear() {
        clear(prefs, valueKey)
    }
}

class Prefs private constructor(private val prefsFile: String) {
    companion object {
        infix fun with(prefsFile: String) = Prefs(prefsFile)
    }

    val file by lazy { getPrefs(prefsFile)!! }

    private fun getPrefs(prefsFile: String) =
            Boilerplate.context.getSharedPreferences(prefsFile, MODE_PRIVATE)

    infix fun <T> open(valueKey: String) =
            SharedPrefsDelegate<T>(this, valueKey)

}
