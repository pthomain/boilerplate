package uk.co.glass_software.android.boilerplate.log


import android.util.Log
import java.util.*

class SimpleLogger(private val isDebug: Boolean,
                   private val printer: Printer) : Logger {

    companion object {
        private const val PREFIX = "BLOG"
        private const val MESSAGE_LENGTH_LIMIT = 4000
        private const val FORCE_STACK_TRACE_OUTPUT = false
        private const val STACK_TRACE_DESCRIPTION_LENGTH = 4
        private val packageName = SimpleLogger::class.java.`package`.name
    }

    private val throwablePrinter = ThrowablePrinter(printer)

    constructor(isDebug: Boolean) : this(isDebug, PREFIX)

    constructor(isDebug: Boolean,
                prefix: String) : this(
            isDebug,
            object : Printer {
                override fun print(priority: Int,
                                   tag: String,
                                   message: String) {
                    if (!message.contains(packageName))
                        Log.println(priority, decorate(tag), message)
                }

                private fun decorate(tag: String): String = "[$prefix:$tag]"
            }
    )

    private fun getTag(caller: Any): String {
        if (caller is String) {
            return caller
        }
        val callerClass = caller as? Class<*> ?: caller.javaClass
        return callerClass.name
    }

    override fun d(caller: Any,
                   message: String) {
        if (FORCE_STACK_TRACE_OUTPUT) {
            e(caller, message)
        } else {
            d(caller, message, false)
        }
    }

    fun d(caller: Any,
          message: String,
          forceOutput: Boolean) {
        d(
                getTag(caller),
                message,
                forceOutput
        )
    }

    override fun e(caller: Any,
                   t: Throwable,
                   message: String?) {
        e(
                getTag(caller),
                t,
                message,
                true
        )
    }

    fun e(caller: Any,
          t: Throwable,
          message: String?,
          forceOutput: Boolean) {
        e(
                getTag(caller),
                t,
                message,
                forceOutput
        )
    }

    override fun e(caller: Any,
                   message: String) {
        try {
            throw Logger.LogException(message)
        } catch (e: Logger.LogException) {
            e(getTag(caller), e, message)
        }
    }

    private fun e(tag: String,
                  t: Throwable,
                  message: String?,
                  forceOutput: Boolean = true) {
        log(
                Log.ERROR,
                tag,
                message ?: t.message ?: "",
                t,
                forceOutput
        )
    }

    private fun d(tag: String,
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

    private fun log(priority: Int,
                    tag: String,
                    message: String,
                    throwable: Throwable?,
                    forceOutput: Boolean) {
        if (isDebug || forceOutput) {
            try {
                var file: String? = null
                var line: Int? = null
                try {
                    throw Exception()
                } catch (e: Exception) {
                    val stackTrace = e.stackTrace

                    val index = STACK_TRACE_DESCRIPTION_LENGTH

                    if (stackTrace.size > index) {
                        line = stackTrace[index].lineNumber
                        file = stackTrace[index].fileName
                    }
                }

                logInternal(
                        priority,
                        tag,
                        " ($file:$line) $message",
                        throwable
                )
            } catch (e: MissingFormatArgumentException) {
                e(SimpleLogger::class.java, e, e.message ?: "")
            }
        }
    }

    private fun logInternal(priority: Int,
                            tag: String,
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
