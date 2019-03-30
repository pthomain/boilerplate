package uk.co.glass_software.android.boilerplate.utils.io

import uk.co.glass_software.android.boilerplate.utils.log.Logger
import java.io.Closeable

const val TAG = "IoUtils"

inline fun <T : Closeable?, R> T.useAndLogError(
        block: (T) -> R,
        logger: Logger? = null
) =
        try {
            use(block)
        } catch (e: Exception) {
            logger?.e(TAG, "Caught an IO exception")
            throw e
        }

inline fun <T : Closeable?, R> T.useAndIgnoreError(
        block: (T) -> R,
        logger: Logger? = null
) =
        try {
            useAndLogError(block, logger)
        } catch (e: Exception) {
            null
        }
