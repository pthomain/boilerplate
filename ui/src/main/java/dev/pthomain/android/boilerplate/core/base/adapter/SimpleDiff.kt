package dev.pthomain.android.boilerplate.core.base.adapter

import androidx.recyclerview.widget.DiffUtil

class SimpleDiff<T, I>(
        private val idExtractor: (T) -> I,
        private val sameItemPredicate: (T, T) -> Boolean
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T,
                                 newItem: T) =
            idExtractor(oldItem) == idExtractor(newItem)

    override fun areContentsTheSame(oldItem: T,
                                    newItem: T) =
            sameItemPredicate(oldItem, newItem)
}