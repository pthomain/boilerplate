package dev.pthomain.android.boilerplate.core.utils.log


import android.util.Log
import dev.pthomain.android.boilerplate.core.utils.log.Logger.LogException

class SimpleLogger(private val isDebug: Boolean,
                   packageName: String,
                   private val printer: Printer = SimplePrinter(packageName),
                   private val listener: Listener? = null,
                   private var showDebugStackTrace: Boolean = false) : Logger {

    companion object {
        private const val MESSAGE_LENGTH_LIMIT = 4000
    }

    interface Listener {
        fun log(priority: Int,
                tag: String?,
                message: String,
                throwable: Throwable?)
    }

    private val throwablePrinter = ThrowablePrinter(printer, packageName)

    override fun d(tagOrCaller: Any,
                   message: String) {
        debug(
                tagOrCaller,
                message,
                showDebugStackTrace
        )
    }

    override fun e(tagOrCaller: Any,
                   message: String) {
        try {
            throw LogException(message)
        } catch (e: LogException) {
            e(
                    tagOrCaller,
                    e,
                    message
            )
        }
    }

    override fun e(tagOrCaller: Any,
                   t: Throwable,
                   message: String?) {
        error(
                tagOrCaller,
                t,
                message,
                true
        )
    }

    private fun debug(tagOrCaller: Any,
                      message: String,
                      forceOutput: Boolean) {
        log(
                Log.DEBUG,
                getTag(tagOrCaller),
                message,
                null,
                forceOutput
        )
    }

    private fun error(tagOrCaller: Any,
                      t: Throwable,
                      message: String?,
                      forceOutput: Boolean) {
        log(
                Log.ERROR,
                getTag(tagOrCaller),
                message ?: t.message ?: "",
                t,
                forceOutput
        )
    }

    private fun getTag(caller: Any): String {
        if (caller is String) {
            return caller
        }
        val callerClass = caller as? Class<*> ?: caller.javaClass
        return callerClass.name
    }

    private fun log(priority: Int,
                    tag: String,
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
                            pair.first,
                            output,
                            throwable
                    )
                }
            } catch (e: Exception) {
                e(
                        tag,
                        LogException(
                                "An error occurred trying to log a previous error",
                                e
                        )
                )
            }
        }
    }

    private fun logInternal(priority: Int,
                            tag: String,
                            targetClassName: String,
                            message: String,
                            throwable: Throwable?) {
        if (message.length > MESSAGE_LENGTH_LIMIT) {
            printer.print(
                    priority,
                    tag,
                    targetClassName,
                    message.substring(0, MESSAGE_LENGTH_LIMIT)
            )

            logInternal(
                    priority,
                    tag,
                    targetClassName,
                    message.substring(MESSAGE_LENGTH_LIMIT),
                    throwable
            )
        } else {
            printer.print(
                    priority,
                    tag,
                    targetClassName,
                    message
            )
            throwable?.let { throwablePrinter.printStackTrace(tag, it) }
        }
    }

}
