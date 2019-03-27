package uk.co.glass_software.android.boilerplate.utils.delegates

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@SuppressLint("ApplySharedPref") //Apply is not thread-safe
class SharedPrefsDelegate<T>(private val prefs: Prefs,
                             private val valueKey: String)
    : ReadWriteProperty<Any, T?> {

    companion object {
        inline infix fun <reified T> Prefs.with(valueKey: String): SharedPrefsDelegate<T> =
                SharedPrefsDelegate(this, valueKey)

        fun Prefs.clear(valueKey: String) {
            file.edit().remove(valueKey).commit()
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
        prefs.clear(valueKey)
    }
}

class Prefs private constructor(
        private val prefsFile: String,
        private val context: Context
) {
    companion object {
        infix fun Context.prefs(prefsFile: String) = Prefs(prefsFile, this)
    }

    val file by lazy { getPrefs(prefsFile, context)!! }

    private fun getPrefs(prefsFile: String, context: Context) =
            context.getSharedPreferences(prefsFile, MODE_PRIVATE)

    infix fun <T> open(valueKey: String) =
            SharedPrefsDelegate<T>(this, valueKey)

}
