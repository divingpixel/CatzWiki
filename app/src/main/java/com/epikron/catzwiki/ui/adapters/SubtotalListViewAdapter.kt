package com.epikron.catzwiki.ui.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epikron.catzwiki.ui.views.PriceLineView
import com.epikron.catzwiki.ui.views.PriceLineViewData

class SubtotalListViewAdapter : RecyclerView.Adapter<SubtotalListViewAdapter.ItemHolder>() {

	private var dataSource: MutableList<PriceLineViewData> = mutableListOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
		return ItemHolder(PriceLineView(parent.context))
	}

	override fun onBindViewHolder(holder: ItemHolder, position: Int) {
		holder.bindItem(dataSource[position])
	}

	override fun getItemCount() = dataSource.size

	@SuppressLint("NotifyDataSetChanged")
	fun setData(data: List<PriceLineViewData>) {
		dataSource.clear()
		dataSource.addAll(data)
		notifyDataSetChanged()
	}

	inner class ItemHolder(private val catButton: PriceLineView) :
		RecyclerView.ViewHolder(catButton) {
		fun bindItem(data: PriceLineViewData) {
			catButton.state = data
		}
	}
}
