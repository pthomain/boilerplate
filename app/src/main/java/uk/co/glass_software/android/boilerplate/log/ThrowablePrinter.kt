package uk.co.glass_software.android.boilerplate.log

import android.annotation.TargetApi
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT
import android.util.Log
import java.util.*

//Copied and modified from java.util
internal class ThrowablePrinter(private val printer: Printer) {

    companion object {
        private const val CAUSE_CAPTION = "Caused by: "
        private const val SUPPRESSED_CAPTION = "Suppressed: "
    }

    internal fun printStackTrace(tag: String?,
                                 throwable: Throwable) {
        val dejaVu = Collections.newSetFromMap(IdentityHashMap<Throwable, Boolean>())
        dejaVu.add(throwable)

        printer.print(
                Log.ERROR,
                tag,
                "${throwable::class.qualifiedName}: ${throwable.message}"
        )

        val trace = throwable.stackTrace
        for (traceElement in trace) {
            printer.print(
                    Log.ERROR,
                    tag,
                    "\tat $traceElement"
            )
        }

        if (SDK_INT >= KITKAT) {
            for (se in throwable.suppressed) {
                printEnclosedStackTrace(
                        throwable,
                        tag,
                        trace,
                        SUPPRESSED_CAPTION,
                        "\t", dejaVu
                )
            }
        }

        // Print cause, if any
        throwable.cause?.let {
            printEnclosedStackTrace(
                    it,
                    tag,
                    trace,
                    CAUSE_CAPTION,
                    "",
                    dejaVu
            )
        }
    }

    @TargetApi(KITKAT)
    private fun printEnclosedStackTrace(throwable: Throwable,
                                        tag: String?,
                                        enclosingTrace: Array<StackTraceElement>,
                                        caption: String,
                                        prefix: String,
                                        dejaVu: MutableSet<Throwable>) {
        if (dejaVu.contains(throwable)) {
            printer.print(
                    Log.ERROR,
                    tag,
                    "\t[CIRCULAR REFERENCE:$throwable]"
            )
        } else {
            dejaVu.add(throwable)
            // Compute number of frames in common between this and enclosing trace
            val trace = throwable.stackTrace
            var m = trace.size - 1
            var n = enclosingTrace.size - 1
            while (m >= 0 && n >= 0 && trace[m] == enclosingTrace[n]) {
                m--
                n--
            }
            val framesInCommon = trace.size - 1 - m

            // Print our stack trace
            printer.print(
                    Log.ERROR,
                    tag,
                    prefix + caption + this
            )

            for (i in 0..m) {
                printer.print(
                        Log.ERROR,
                        tag,
                        "$prefix\tat ${trace[i]}"
                )
            }

            if (framesInCommon != 0) {
                printer.print(
                        Log.ERROR,
                        tag,
                        "$prefix\t... $framesInCommon more"
                )
            }

            // Print suppressed exceptions, if any
            for (se in throwable.suppressed)
                printEnclosedStackTrace(
                        throwable,
                        tag,
                        trace,
                        SUPPRESSED_CAPTION,
                        "$prefix\t",
                        dejaVu
                )

            // Print cause, if any
            throwable.cause?.let {
                printEnclosedStackTrace(
                        it,
                        tag,
                        trace,
                        CAUSE_CAPTION,
                        prefix,
                        dejaVu
                )
            }
        }
    }
}
