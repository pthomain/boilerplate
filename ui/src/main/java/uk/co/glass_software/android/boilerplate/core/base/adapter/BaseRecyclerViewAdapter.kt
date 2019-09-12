package uk.co.glass_software.android.boilerplate.core.base.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VH : BaseViewHolder<T>>(
        private val itemListHolder: ItemListHolder<T> = SimpleItemListHolder(),
        //This is needed to propagate the current index to the wrapping BasePagingAdapter
        private val itemIndexCallback: (Int) -> Unit = { Unit }
) : RecyclerView.Adapter<VH>(),
        ItemListHolder<T> by itemListHolder {

    final override fun getItemCount() = items.size
    open fun processItems(items: List<T>) = items

    final override fun onBindViewHolder(holder: VH,
                                        position: Int) {
        holder.populate(getItem(position))
    }

    final override fun onBindViewHolder(holder: VH,
                                        position: Int,
                                        payloads: MutableList<Any>) {
        onBindViewHolder(holder, position)
    }

    fun update(items: List<T>) {
        this.items.clear()
        this.items.addAll(processItems(items))
        notifyDataSetChanged()
    }

    // Needed to propagate the current index to BasePagingAdapter
    // see https://stackoverflow.com/questions/49421587/android-google-paging-library-not-working
    private fun getItem(position: Int): T {
        itemIndexCallback(position)
        return items[position]
    }
}

