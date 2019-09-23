package dev.pthomain.android.boilerplate.core.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<M>(parent: ViewGroup,
                                 @LayoutRes layout: Int)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(
                        layout,
                        parent,
                        false
                )
) {

    abstract fun populate(model: M)

}