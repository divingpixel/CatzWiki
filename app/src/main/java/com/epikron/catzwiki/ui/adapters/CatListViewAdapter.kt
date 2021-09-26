package com.epikron.catzwiki.ui.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epikron.catzwiki.ui.views.CatButtonView
import com.epikron.catzwiki.ui.views.CatButtonViewData

class CatListViewAdapter : RecyclerView.Adapter<CatListViewAdapter.ItemHolder>() {

	private var dataSource: MutableList<CatButtonViewData> = mutableListOf()

	var onItemClickListener: ((position: Int, item: CatButtonViewData) -> Unit)? = null

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
		return ItemHolder(CatButtonView(parent.context))
	}

	override fun onBindViewHolder(holder: ItemHolder, position: Int) {
		holder.bindItem(position, dataSource[position])
	}

	override fun getItemCount() = dataSource.size

	@SuppressLint("NotifyDataSetChanged")
	fun setData(data: List<CatButtonViewData>) {
		dataSource.clear()
		dataSource.addAll(data)
		notifyDataSetChanged()
	}

	inner class ItemHolder(private val catButton: CatButtonView) :
		RecyclerView.ViewHolder(catButton) {
		fun bindItem(position: Int, data: CatButtonViewData) {
			catButton.state = data
			catButton.onClickListener = { onItemClickListener?.invoke(position, data) }
		}
	}
}
