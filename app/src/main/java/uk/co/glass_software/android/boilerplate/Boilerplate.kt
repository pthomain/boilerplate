package uk.co.glass_software.android.boilerplate

import android.annotation.SuppressLint
import android.content.Context
import uk.co.glass_software.android.boilerplate.utils.log.Logger
import uk.co.glass_software.android.boilerplate.utils.log.SimpleLogger
import uk.co.glass_software.android.boilerplate.utils.rx.observeNetworkAvailability
import uk.co.glass_software.android.boilerplate.utils.string.capitaliseFirst

@SuppressLint("StaticFieldLeak") //Application context cannot leak
object Boilerplate {

    var isInitialised = false
        private set

    lateinit var context: Context
        private set

    lateinit var logger: Logger
        private set

    var networkAvailable: Boolean = false
        private set

    @Synchronized
    fun init(context: Context,
             isDebug: Boolean,
             logPrefix: String? = null) {
        if (!isInitialised) {
            this.context = context.applicationContext

            logger = SimpleLogger(
                    isDebug,
                    getLogPrefix(logPrefix),
                    context.packageName
            )

            observeNetwork()
            isInitialised = true
        }
    }

    @SuppressLint("CheckResult")
    private fun observeNetwork() {
        observeNetworkAvailability().subscribe(
                { networkAvailable = it },
                { logger.e(this, it, "An error occurred while observing the network connectivity") }
        )
    }

    private fun getLogPrefix(logPrefix: String?) =
            logPrefix ?: getPackageName().capitaliseFirst().plus("Log")

    private fun getPackageName() =
            context.packageName.let {
                it.substring(it.lastIndexOf(".") + 1)
            }

}