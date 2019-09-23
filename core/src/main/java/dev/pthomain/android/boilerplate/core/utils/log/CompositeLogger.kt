package dev.pthomain.android.boilerplate.core.utils.log

class CompositeLogger(private vararg val loggers: Logger) : Logger {

    override fun e(tagOrCaller: Any,
                   t: Throwable,
                   message: String?) {
        loggers.forEach { it.e(tagOrCaller, t, message) }
    }

    override fun e(tagOrCaller: Any, message: String) {
        loggers.forEach { it.e(tagOrCaller, message) }
    }

    override fun d(tagOrCaller: Any, message: String) {
        loggers.forEach { it.d(tagOrCaller, message) }
    }

}