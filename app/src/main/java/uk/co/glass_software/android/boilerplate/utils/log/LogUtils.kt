package uk.co.glass_software.android.boilerplate.utils.log

import uk.co.glass_software.android.boilerplate.Boilerplate.logger

fun Throwable?.log(tagOrCaller: Any,
                   message: String? = this?.message) {
    this?.let { logger.e(tagOrCaller, it, message) }
}

fun String?.logDebug(tagOrCaller: Any) {
    this?.let { logger.d(tagOrCaller, it) }
}

fun String?.logError(tagOrCaller: Any) {
    this?.let { logger.e(tagOrCaller, it) }
}
