package com.epikron.catzwiki.ui.adapters

import androidx.recyclerview.widget.DiffUtil

class DiffCallback <T> (private val oldList: List<T>, private val newList: List<T>) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
    }

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
}