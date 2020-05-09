package dev.pthomain.android.boilerplate.ui.interactors.paging

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import dev.pthomain.android.boilerplate.core.utils.rx.On
import dev.pthomain.android.boilerplate.ui.interactors.ObservableInteractor
import dev.pthomain.android.boilerplate.ui.interactors.SingleInteractor

abstract class BasePagingInteractor<R, D>(
        params: Parameters = Parameters()
) : ObservableInteractor<PagedList<D>> {

    private val dataSourceFactory = object : DataSource.Factory<Parameters, D>() {
        override fun create() = PagingDataSource(
                params,
                ::newInteractor,
                ::responseToList
        )
    }

    private val config =
            PagedList.Config.Builder()
                    .setEnablePlaceholders(true)
                    .setPageSize(params.pageSize)
                    .build()

    override fun call() =
            RxPagedListBuilder(dataSourceFactory, config)
                    .setFetchScheduler(On.Io())
                    .buildObservable()

    protected abstract fun newInteractor(params: Parameters): SingleInteractor<R>
    protected abstract fun responseToList(response: R): MutableList<D>

}