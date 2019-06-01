package uk.co.glass_software.android.boilerplate.core.utils.log

import android.util.Log

internal class SimplePrinter(
        private val packageName: String
) : Printer {

    override fun canPrint(className: String) =
            !className.contains(SimpleLogger::class.java.`package`!!.name)

    override fun print(priority: Int,
                       tag: String?,
                       targetClassName: String,
                       message: String) {
        if (tag?.let { canPrint(it) } != false)
            Log.println(
                    priority,
                    cleanUpTag(tag),
                    message.trim()
            )
    }

    private fun cleanUpTag(tag: String?) =
            tag?.replace("$packageName.", "")?.let {
                val start = it.length - MAX_TAG_LENGTH

                val truncated = if (start > 0) {
                    it.substring(start, it.length).let {
                        if (it.contains(".")) {
                            it.substring(it.indexOf(".") + 1)
                        } else it
                    }
                } else it

                if (truncated.contains(".")) {
                    truncated.substring(0, truncated.lastIndexOf("."))
                } else truncated
            }
}

private const val MAX_TAG_LENGTH = 88

