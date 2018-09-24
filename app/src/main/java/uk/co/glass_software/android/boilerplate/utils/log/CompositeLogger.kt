package uk.co.glass_software.android.boilerplate.utils.log

class CompositeLogger(private vararg val loggers: Logger) : Logger {

    override fun e(tag: String,
                   t: Throwable,
                   message: String?) {
        loggers.forEach { it.e(tag, t, message) }
    }

    override fun e(t: Throwable, message: String?) {
        loggers.forEach { it.e(t, message) }
    }

    override fun e(tag: String, message: String) {
        loggers.forEach { it.e(tag, message) }
    }

    override fun e(message: String) {
        loggers.forEach { it.e(message) }
    }

    override fun d(tag: String, message: String) {
        loggers.forEach { it.d(tag, message) }
    }

    override fun d(message: String) {
        loggers.forEach { it.d(message) }
    }

}