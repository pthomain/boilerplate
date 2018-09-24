package uk.co.glass_software.android.boilerplate

import android.annotation.SuppressLint
import android.content.Context
import uk.co.glass_software.android.boilerplate.utils.log.Logger
import uk.co.glass_software.android.boilerplate.utils.log.SimpleLogger
import uk.co.glass_software.android.boilerplate.utils.string.capitaliseFirst

@SuppressLint("StaticFieldLeak") //Application context cannot leak
object Boilerplate {

    var isInitialised = false
        private set

    lateinit var context: Context
    lateinit var logger: Logger

    @Synchronized
    fun init(context: Context,
             isDebug: Boolean,
             logPrefix: String? = null) {
        if (!isInitialised) {
            this.context = context.applicationContext
            logger = SimpleLogger(isDebug, getLogPrefix(logPrefix))
            isInitialised = true
        }
    }

    private fun getLogPrefix(logPrefix: String?) =
            logPrefix ?: getPackageName().capitaliseFirst().plus("Log")

    private fun getPackageName() =
            context.packageName.let {
                it.substring(it.lastIndexOf(".") + 1)
            }

}