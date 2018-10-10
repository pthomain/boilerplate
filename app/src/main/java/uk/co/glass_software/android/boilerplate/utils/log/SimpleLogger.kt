package uk.co.glass_software.android.boilerplate.utils.log


import android.util.Log
import uk.co.glass_software.android.boilerplate.utils.log.Logger.LogException

class SimpleLogger(private val isDebug: Boolean,
                   private val printer: Printer,
                   private val listener: Listener? = null,
                   private var showDebugStackTrace: Boolean = false) : Logger {

    companion object {
        private const val MESSAGE_LENGTH_LIMIT = 4000
        private val packageName = SimpleLogger::class.java.`package`!!.name
    }

    interface Listener {
        fun log(priority: Int,
                tag: String?,
                message: String,
                throwable: Throwable?)
    }

    private val throwablePrinter = ThrowablePrinter(printer)

    constructor(isDebug: Boolean,
                prefix: String) : this(
            isDebug,
            object : Printer {
                override fun canPrint(className: String) = !className.contains(packageName)

                override fun print(priority: Int,
                                   tag: String?,
                                   message: String) {
                    if (canPrint(message))
                        Log.println(priority, decorate(tag), message)
                }

                private fun decorate(tag: String?) = tag?.let { "[$prefix:$tag]" } ?: "[$prefix]"
            }
    )

    override fun d(tag: String,
                   message: String) {
        debug(
                tag,
                message,
                showDebugStackTrace
        )
    }

    override fun d(message: String) {
        debug(
                null,
                message,
                showDebugStackTrace
        )
    }

    private fun debug(tag: String?,
                      message: String,
                      forceOutput: Boolean) {
        log(
                Log.DEBUG,
                tag,
                message,
                null,
                forceOutput
        )
    }

    override fun e(tag: String,
                   t: Throwable,
                   message: String?) {
        error(
                tag,
                t,
                message,
                true
        )
    }

    override fun e(t: Throwable,
                   message: String?) {
        error(
                null,
                t,
                message,
                true
        )
    }

    override fun e(tag: String,
                   message: String) {
        try {
            throw LogException(message)
        } catch (e: LogException) {
            e(
                    tag,
                    e,
                    message
            )
        }
    }

    override fun e(message: String) {
        try {
            throw LogException(message)
        } catch (e: LogException) {
            e(e, message)
        }
    }

    private fun error(tag: String?,
                      t: Throwable,
                      message: String?,
                      forceOutput: Boolean) {
        log(
                Log.ERROR,
                tag,
                message ?: t.message ?: "",
                t,
                forceOutput
        )
    }

    private fun log(priority: Int,
                    tag: String?,
                    message: String,
                    throwable: Throwable?,
                    forceOutput: Boolean) {
        if (isDebug || forceOutput) {
            try {
                try {
                    throw Exception()
                } catch (e: Exception) {
                    var index = 0

                    for (x in 0..e.stackTrace.size) {
                        if (printer.canPrint(e.stackTrace[x].className)) {
                            index = x
                            break
                        }
                    }

                    Pair(e.stackTrace[index].fileName, e.stackTrace[index].lineNumber)
                }.let { pair ->
                    val output = " (${pair.first}:${pair.second}) $message"
                    listener?.log(
                            priority,
                            tag,
                            output,
                            throwable
                    )
                    logInternal(
                            priority,
                            tag,
                            output,
                            throwable
                    )
                }
            } catch (e: Exception) {
                val logException = LogException(
                        "An error occurred trying to log a previous error",
                        e
                )
                e(logException)
            }
        }
    }

    private fun logInternal(priority: Int,
                            tag: String?,
                            message: String,
                            throwable: Throwable?) {
        if (message.length > MESSAGE_LENGTH_LIMIT) {
            printer.print(
                    priority,
                    tag,
                    message.substring(0, MESSAGE_LENGTH_LIMIT)
            )

            logInternal(priority,
                    tag,
                    message.substring(MESSAGE_LENGTH_LIMIT),
                    throwable
            )
        } else {
            printer.print(priority, tag, message)
            throwable?.let { throwablePrinter.printStackTrace(tag, it) }
        }
    }

}
