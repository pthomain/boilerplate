package uk.co.glass_software.android.boilerplate.utils.log

import uk.co.glass_software.android.boilerplate.Boilerplate

fun Boilerplate.log(tagOrCaller: Any,
                    message: String) {
    logger.d(tagOrCaller, message)
}

fun Boilerplate.log(tagOrCaller: Any,
                    throwable: Throwable,
                    message: String? = throwable.message) {
    logger.e(tagOrCaller, throwable, message)
}
