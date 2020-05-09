package dev.pthomain.android.boilerplate.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.webkit.WebView
import androidx.annotation.CallSuper
import dev.pthomain.android.boilerplate.core.utils.lambda.Action
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import dev.pthomain.android.boilerplate.core.utils.rx.ioUi
import dev.pthomain.android.boilerplate.core.utils.rx.observeNetworkAvailability
import io.reactivex.disposables.Disposables

open class NetworkAwareWebView @JvmOverloads constructor(context: Context,
                                                         attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {

    private var pendingAction: Action? = null
    private var subscription = Disposables.disposed()

    private var isConnected: Boolean = true
    var logger: Logger? = null

    init {
        observeNetwork()
    }

    @CallSuper
    override fun loadUrl(url: String?,
                         additionalHttpHeaders: MutableMap<String, String>?) {
        waitForNetwork {
            super.loadUrl(url, additionalHttpHeaders)
        }
    }

    @CallSuper
    override fun loadUrl(url: String?) {
        waitForNetwork {
            super.loadUrl(url)
        }
    }

    @CallSuper
    override fun reload() {
        waitForNetwork {
            super.reload()
        }
    }

    @CallSuper
    override fun postUrl(url: String?,
                         postData: ByteArray?) {
        waitForNetwork {
            super.postUrl(url, postData)
        }
    }

    @CallSuper
    override fun loadData(data: String?,
                          mimeType: String?,
                          encoding: String?) {
        waitForNetwork {
            super.loadData(
                    data,
                    mimeType,
                    encoding
            )
        }
    }

    @CallSuper
    override fun loadDataWithBaseURL(baseUrl: String?,
                                     data: String?,
                                     mimeType: String?,
                                     encoding: String?,
                                     historyUrl: String?) {
        waitForNetwork {
            super.loadDataWithBaseURL(
                    baseUrl,
                    data,
                    mimeType,
                    encoding,
                    historyUrl
            )
        }
    }

    private fun waitForNetwork(action: () -> Unit) {
        pendingAction = Action.From(action)

        if (isConnected) {
            setNetworkAvailable(true)
            pendingAction?.invoke()
            pendingAction = null
        } else {
            setNetworkAvailable(false)
            subscription = context.observeNetworkAvailability()
                    .filter { it }
                    .firstOrError()
                    .ioUi()
                    .subscribe(
                            { executePendingAction() },
                            { logger?.e(this, it, "Could not load URL") }
                    )
        }
    }

    @SuppressLint("CheckResult")
    private fun observeNetwork() {
        context.observeNetworkAvailability().subscribe(
                { isConnected = it },
                { logger?.e(this, it, "An error occurred while observing the network connectivity") }
        )
    }

    private fun executePendingAction() {
        pendingAction?.let { waitForNetwork(it) }
    }

    @CallSuper
    override fun onSaveInstanceState(): Parcelable? {
        if (!subscription.isDisposed) {
            subscription.dispose()
        }
        return super.onSaveInstanceState()
    }

    @CallSuper
    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        executePendingAction()
    }
}