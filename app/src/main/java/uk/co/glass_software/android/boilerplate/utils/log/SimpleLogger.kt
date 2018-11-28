package uk.co.glass_software.android.boilerplate.utils.log


import android.util.Log
import uk.co.glass_software.android.boilerplate.utils.log.Logger.LogException

class SimpleLogger(private val isDebug: Boolean,
                   private val packageName: String,
                   private val printer: Printer,
                   private val listener: Listener? = null,
                   private var showDebugStackTrace: Boolean = false) : Logger {

    companion object {
        private const val MESSAGE_LENGTH_LIMIT = 4000
        private const val TAG_LENGTH_LIMIT = 88
    }

    interface Listener {
        fun log(priority: Int,
                tag: String?,
                message: String,
                throwable: Throwable?)
    }

    private val throwablePrinter = ThrowablePrinter(printer, packageName)

    constructor(isDebug: Boolean,
                prefix: String,
                packageName: String) : this(
            isDebug,
            packageName,
            object : Printer {

                private val prefixLength = prefix.length + 1

                override fun canPrint(className: String) =
                        !className.contains(SimpleLogger::class.java.`package`!!.name)

                override fun print(priority: Int,
                                   tag: String?,
                                   targetClassName: String,
                                   message: String) {
                    if (canPrint(message))
                        Log.println(
                                priority,
                                decorate(tag, targetClassName),
                                message
                        )
                }

                private fun decorate(tag: String?,
                                     targetClassName: String) =
                        trimTag(tag, targetClassName)?.let { "[$prefix:$it]" } ?: "[$prefix]"

                private fun trimTag(tag: String?,
                                    targetClassName: String) =
                        tag?.replace(Regex("^$packageName\\."), "")
                                ?.replace(Regex("\\.$targetClassName\$"), "")
                                ?.let { trimTagToMaxLength(it) }

                private fun trimTagToMaxLength(tag: String): String =
                        if (tag.length + prefixLength > TAG_LENGTH_LIMIT) {
                            if (tag.contains("."))
                                trimTagToMaxLength(tag.substring(tag.indexOf(".")))
                            else
                                tag.substring(tag.length - TAG_LENGTH_LIMIT, tag.length)
                        } else tag
            }
    )

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

            logInternal(priority,
                    tag,
                    targetClassName,
                    message.substring(MESSAGE_LENGTH_LIMIT),
                    throwable
            )
        } else {
            printer.print(priority, tag, message, packageName)
            throwable?.let { throwablePrinter.printStackTrace(tag, it) }
        }
    }

}
