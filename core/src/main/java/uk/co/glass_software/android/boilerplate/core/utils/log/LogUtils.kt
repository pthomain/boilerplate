package uk.co.glass_software.android.boilerplate.core.utils.log

import android.content.Context
import uk.co.glass_software.android.boilerplate.core.utils.string.capitaliseFirst

fun Context.simpleLogger(isDebug: Boolean,
                         logPrefix: String? = null) = SimpleLogger(
        isDebug,
        getLogPrefix(logPrefix),
        packageName
)

private fun Context.getLogPrefix(logPrefix: String?) =
        logPrefix ?: getPackageNameForLogging().capitaliseFirst().plus("Log")

private fun Context.getPackageNameForLogging() =
        packageName.let {
            it.substring(it.lastIndexOf(".") + 1)
        }