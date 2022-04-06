package com.epikron.catzwiki.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.epikron.catzwiki.ui.views.CatButtonView
import com.epikron.catzwiki.ui.views.CatButtonView.*

class CatListViewAdapter : RecyclerView.Adapter<CatListViewAdapter.ItemHolder>() {

	private var dataSource: MutableList<CatButtonViewData> = mutableListOf()

	var onItemClickListener: ((position: Int, item: CatButtonViewData) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemHolder(CatButtonView(parent.context))

	override fun onBindViewHolder(holder: ItemHolder, position: Int) = holder.bindItem(position, dataSource[position])

	override fun getItemCount() = dataSource.size

	fun setData(data: List<CatButtonViewData>) {
		val diffCallback = DiffCallback(dataSource, data)
		val diffResult = DiffUtil.calculateDiff(diffCallback)
		dataSource.clear()
		dataSource.addAll(data)
		diffResult.dispatchUpdatesTo(this)
	}

	inner class ItemHolder(private val catButton: CatButtonView) :
		RecyclerView.ViewHolder(catButton) {
		fun bindItem(position: Int, data: CatButtonViewData) {
			catButton.data = data
			catButton.onClickListener = { onItemClickListener?.invoke(position, data) }
		}
	}
}
