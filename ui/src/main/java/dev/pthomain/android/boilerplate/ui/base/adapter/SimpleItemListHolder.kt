package dev.pthomain.android.boilerplate.ui.base.adapter

class SimpleItemListHolder<T> : ItemListHolder<T> {
    override val items = mutableListOf<T>()
}