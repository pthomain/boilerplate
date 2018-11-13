package uk.co.glass_software.android.boilerplate.utils.log

interface Logger {

    fun e(tagOrCaller: Any,
          t: Throwable,
          message: String? = t.message)

    fun e(tagOrCaller: Any,
          message: String)

    fun d(tagOrCaller: Any,
          message: String)

    class LogException internal constructor(detailMessage: String,
                                            cause: Throwable? = null)
        : Exception(detailMessage) {
        init {
            if (cause != null) initCause(cause)
        }
    }

}