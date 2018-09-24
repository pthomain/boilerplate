package uk.co.glass_software.android.boilerplate.utils.log

interface Logger {

    fun e(tag: String,
          t: Throwable,
          message: String? = t.message)

    fun e(t: Throwable,
          message: String? = t.message)

    fun e(tag: String,
          message: String)

    fun e(message: String)

    fun d(tag: String,
          message: String)

    fun d(message: String)

    class LogException internal constructor(detailMessage: String,
                                            cause: Throwable? = null)
        : Exception(detailMessage) {
        init {
            if (cause != null) initCause(cause)
        }
    }

}