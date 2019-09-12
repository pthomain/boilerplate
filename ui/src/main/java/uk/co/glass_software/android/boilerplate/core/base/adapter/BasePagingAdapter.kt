package uk.co.glass_software.android.boilerplate.core.base.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<T, I, VH : RecyclerView.ViewHolder, R : RecyclerView.Adapter<VH>>(
        idMapper: (T) -> I,
        itemIdentityChecker: (T, T) -> Boolean = { t1, t2 -> idMapper(t1) == idMapper(t2) }
) : PagedListAdapter<T, VH>(SimpleDiff(idMapper, itemIdentityChecker)),
        ItemListHolder<T> {

    private val wrappedAdapter: R by lazy { createInternalAdapter { getItem(it) } }

    final override val items: MutableList<T>
        get() = currentList ?: arrayListOf()

    final override fun getItemViewType(position: Int) =
            wrappedAdapter.getItemViewType(position)

    final override fun getItem(position: Int): T {
        super.getItem(position)
        return items[position]
    }

    final override fun onCreateViewHolder(parent: ViewGroup,
                                          viewType: Int) =
            wrappedAdapter.onCreateViewHolder(parent, viewType)

    final override fun onBindViewHolder(holder: VH,
                                        position: Int) {
        wrappedAdapter.onBindViewHolder(holder, position)
    }

    // Any wrapped adapter must propagate the current item's index: itemIndexCallback must be provided
    // in the constructor of the wrapped BaseRecyclerViewAdapter.
    // see https://stackoverflow.com/questions/49421587/android-google-paging-library-not-working
    protected abstract fun createInternalAdapter(itemIndexCallback: (Int) -> Unit): R

}