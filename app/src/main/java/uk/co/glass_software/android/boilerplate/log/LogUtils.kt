package uk.co.glass_software.android.boilerplate.log

import uk.co.glass_software.android.boilerplate.Boilerplate.logger

fun Throwable?.log() {
    log(this?.message)
}

infix fun Throwable?.log(message: String?) {
    this?.let { logger.e(it, message) }
}

fun String?.logDebug() {
    this?.let { logger.d(it) }
}

fun String?.logError() {
    this?.let { logger.e(it) }
}
