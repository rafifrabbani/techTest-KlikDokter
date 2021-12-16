package com.appschef.hospitalapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appschef.hospitalapp.data.remote.model.HospitalListItem
import com.appschef.hospitalapp.databinding.ItemHospitalBinding

class HospitalItemAdapter(
    val onClickGmaps: (HospitalListItem) -> Unit,
    val onDelete: (HospitalListItem) -> Unit,
) : ListAdapter<HospitalListItem, HospitalItemAdapter.HospitalItemViewHolder>(HospitalItemComparator()) {

    class HospitalItemViewHolder(val itemHospitalBinding: ItemHospitalBinding) :
        RecyclerView.ViewHolder(itemHospitalBinding.root)

    class HospitalItemComparator : DiffUtil.ItemCallback<HospitalListItem>() {
        override fun areItemsTheSame(
            oldItem: HospitalListItem,
            newItem: HospitalListItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HospitalListItem,
            newItem: HospitalListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalItemViewHolder {
        return HospitalItemViewHolder(
            ItemHospitalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HospitalItemViewHolder, position: Int) {
        val hospitalItem = getItem(position)
        holder.itemHospitalBinding.apply {
            tvHospitalName.text = hospitalItem.hospitalName
            tvAddress.text = hospitalItem.hospitalAddress
            tvLinkGmaps.text = "show on google maps"
            tvLinkGmaps.setOnClickListener {
                onClickGmaps(hospitalItem)
            }
            ivDelete.setOnClickListener {
                onDelete(hospitalItem)
            }
        }
        holder.itemView.setOnClickListener {
            print("clicked")
            hospitalItem.isExpandable = !hospitalItem.isExpandable
            holder.itemHospitalBinding.expandedView.isVisible = hospitalItem.isExpandable
        }
    }
}