package uk.co.glass_software.android.boilerplate.log

interface Logger {

    fun e(caller: Any,
          t: Throwable,
          message: String? = t.message)

    fun e(caller: Any,
          message: String)

    fun d(caller: Any,
          message: String)

    class LogException internal constructor(detailMessage: String) : Exception(detailMessage)

}