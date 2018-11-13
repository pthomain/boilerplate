package uk.co.glass_software.android.boilerplate.utils.io

import uk.co.glass_software.android.boilerplate.utils.log.log
import java.io.Closeable

const val TAG = "IoUtils"

inline fun <T : Closeable?, R> T.useAndLogError(block: (T) -> R) =
        try {
            use(block)
        } catch (e: Exception) {
            e.log(TAG, "Caught an IO exception")
            throw e
        }

inline fun <T : Closeable?, R> T.useAndIgnoreError(block: (T) -> R) =
        try {
            useAndLogError(block)
        } catch (e: Exception) {
            null
        }
