package uk.co.glass_software.android.boilerplate.log

lateinit var logger: Logger

fun Throwable?.log() {
    log(this?.message)
}

infix fun Throwable?.log(message: String?) {
    this?.let { logger.e(this, this, message) }
}

fun String?.logDebug() {
    this?.let { logger.d(this, this) }
}

fun String?.logError() {
    this?.let { logger.e(this, this) }
}
