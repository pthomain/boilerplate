package uk.co.glass_software.android.boilerplate.utils.io

import uk.co.glass_software.android.boilerplate.Boilerplate
import uk.co.glass_software.android.boilerplate.utils.log.log
import java.io.Closeable

const val TAG = "IoUtils"

inline fun <T : Closeable?, R> T.useAndLogError(
        block: (T) -> R,
        boilerplate: Boilerplate? = null
) =
        try {
            use(block)
        } catch (e: Exception) {
            boilerplate?.log(TAG, "Caught an IO exception")
            throw e
        }

inline fun <T : Closeable?, R> T.useAndIgnoreError(
        block: (T) -> R,
        boilerplate: Boilerplate? = null
) =
        try {
            useAndLogError(block, boilerplate)
        } catch (e: Exception) {
            null
        }
