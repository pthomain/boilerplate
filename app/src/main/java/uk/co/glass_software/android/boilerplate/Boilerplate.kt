package uk.co.glass_software.android.boilerplate

import android.content.Context
import uk.co.glass_software.android.boilerplate.utils.log.Logger
import uk.co.glass_software.android.boilerplate.utils.log.SimpleLogger
import uk.co.glass_software.android.boilerplate.utils.string.capitaliseFirst

class Boilerplate(context: Context,
                  val isDebug: Boolean,
                  logPrefix: String? = null) {

    val context = context.applicationContext
    val logger: Logger

    init {
        logger = SimpleLogger(
                isDebug,
                getLogPrefix(logPrefix),
                context.packageName
        )
    }

    private fun getLogPrefix(logPrefix: String?) =
            logPrefix ?: getPackageName().capitaliseFirst().plus("Log")

    private fun getPackageName() =
            context.packageName.let {
                it.substring(it.lastIndexOf(".") + 1)
            }

}