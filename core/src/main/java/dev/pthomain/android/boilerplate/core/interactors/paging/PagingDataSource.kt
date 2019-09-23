package dev.pthomain.android.boilerplate.core.interactors.paging

import androidx.paging.PageKeyedDataSource
import dev.pthomain.android.boilerplate.core.interactors.SingleInteractor
import io.reactivex.disposables.CompositeDisposable
import kotlin.math.max

internal class PagingDataSource<R, D>(
        private val params: Parameters,
        private val interactorFactory: (Parameters) -> SingleInteractor<R>,
        private val listExtractor: (R) -> MutableList<D>
) : PageKeyedDataSource<Parameters, D>() {

    companion object {
        const val START = 1
    }

    private var disposable = CompositeDisposable()

    override fun invalidate() {
        disposable.dispose()
        disposable = CompositeDisposable()
        super.invalidate()
    }

    private fun nextPage(params: Parameters,
                         step: Int) =
            params.copy(page = params.page + step)

    override fun loadInitial(params: LoadInitialParams<Parameters>,
                             callback: LoadInitialCallback<Parameters, D>) {
        with(this@PagingDataSource.params.let { it.copy(page = max(START, it.page)) }) {
            loadPage(this) {
                callback.onResult(
                        it,
                        null,
                        nextPage(this, 1)
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Parameters>,
                           callback: LoadCallback<Parameters, D>) {
        with(params.key) {
            loadPage(this) {
                callback.onResult(
                        it,
                        nextPage(this, 1)
                )
            }
        }
    }

    override fun loadBefore(params: LoadParams<Parameters>,
                            callback: LoadCallback<Parameters, D>) {
        //Nothing to do, not going backwards
    }

    @Synchronized
    private fun loadPage(params: Parameters,
                         onResult: (MutableList<D>) -> Unit) {
        interactorFactory(params)
                .call()
                .subscribe(
                        { onResult(listExtractor(it)) },
                        {}
                ).also { disposable.add(it) }
    }

}