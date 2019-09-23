package dev.pthomain.android.boilerplate.core.utils.bundle

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

private const val INTENT_BUNDLE = "INTENT_BUNDLE"

//
// Bundle
//

inline fun <reified T> Bundle?.optionalParam(key: String) =
        this?.get(key) as? T

inline fun <reified T> Bundle?.param(key: String) =
        optionalParam<T>(key)
                ?: throw IllegalStateException("Missing parameter $key")

inline fun <reified T : Parcelable> Bundle?.optionalParcelable(key: String) =
        param<Parcelable>(key) as? T?

inline fun <reified T : Parcelable> Bundle?.parcelable(key: String) =
        optionalParcelable<T>(key)
                ?: throw IllegalStateException("Missing parameter $key")

inline fun <reified T : Serializable> Bundle?.optionalSerializable(key: String) =
        param<Serializable>(key) as? T?

inline fun <reified T : Serializable> Bundle?.serializable(key: String) =
        optionalSerializable<T>(key)
                ?: throw IllegalStateException("Missing parameter $key")

//
// Intent
//

fun Intent?.intentBundle() =
        this?.extras?.getBundle(INTENT_BUNDLE)

inline fun <reified T> Intent?.optionalParam(key: String) =
        intentBundle().optionalParam<T>(key)

inline fun <reified T> Intent?.param(key: String) =
        intentBundle().param<T>(key)

inline fun <reified T : Parcelable> Intent?.optionalParcelable(key: String) =
        intentBundle().param<T>(key)

inline fun <reified T : Parcelable> Intent?.parcelable(key: String) =
        intentBundle().optionalParcelable<T>(key)

inline fun <reified T : Serializable> Intent.optionalSerializable(key: String) =
        intentBundle().optionalSerializable<T>(key)

inline fun <reified T : Serializable> Intent.serializable(key: String) =
        intentBundle().serializable<T>(key)

//
// Activity
//

inline fun <reified T> Activity.optionalParam(key: String) =
        intent.optionalParam<T>(key)

inline fun <reified T> Activity.param(key: String) =
        intent.param<T>(key)

inline fun <reified T : Parcelable> Activity.optionalParcelable(key: String) =
        intent.optionalParcelable<T>(key)

inline fun <reified T : Parcelable> Activity.parcelable(key: String) =
        intent.parcelable<T>(key)

inline fun <reified T : Serializable> Activity.optionalSerializable(key: String) =
        intent.param<T>(key)

inline fun <reified T : Serializable> Activity.serializable(key: String) =
        intent.serializable<T>(key)

//
// Fragment
//

inline fun <reified T> Fragment.optionalParam(key: String) =
        arguments.optionalParam<T>(key)

inline fun <reified T> Fragment.param(key: String) =
        arguments.param<T>(key)

inline fun <reified T : Parcelable> Fragment.optionalParcelable(key: String) =
        arguments.optionalParcelable<T>(key)

inline fun <reified T : Parcelable> Fragment.parcelable(key: String) =
        arguments.parcelable<T>(key)

inline fun <reified T : Serializable> Fragment.optionalSerializable(key: String) =
        arguments.optionalSerializable<T>(key)

inline fun <reified T : Serializable> Fragment.serializable(key: String) =
        arguments.serializable<T>(key)

//
// Adding parameters
//

fun <T : Fragment> T.addParams(vararg extra: Pair<String, Any>) = apply {
    if (arguments == null) {
        arguments = Bundle()
    }
    arguments!!.putAll(extra.toMap().toBundle())
}

fun Intent.addParams(vararg extra: Pair<String, Any>) = apply {
    putExtra(INTENT_BUNDLE, extra.toMap().toBundle())
}

fun Map<String, Any>.toBundle() = Bundle().apply {
    entries.forEach {
        when (val value = it.value) {
            is Boolean -> putBoolean(it.key, value)
            is Byte -> putByte(it.key, value)
            is Char -> putChar(it.key, value)
            is Short -> putShort(it.key, value)
            is Int -> putInt(it.key, value)
            is Long -> putLong(it.key, value)
            is Float -> putFloat(it.key, value)
            is Double -> putDouble(it.key, value)
            is String -> putString(it.key, value)
            is CharSequence -> putCharSequence(it.key, value)
            is Parcelable -> putParcelable(it.key, value)
            is Serializable -> putSerializable(it.key, value)
            else -> throw IllegalArgumentException("Unsupported bundle parameter type: $value")
        }
    }
}

