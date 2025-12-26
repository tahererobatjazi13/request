package ir.kitgroup.request.feature.business_side.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import ir.kitgroup.request.core.utils.PersonType
import ir.kitgroup.request.databinding.ItemBusinessSideListBinding

class BusinessSideAdapter(
    private val onEdit: (BusinessSideEntity) -> Unit,
    private val onDelete: (BusinessSideEntity) -> Unit
) : ListAdapter<BusinessSideEntity, BusinessSideAdapter.CustomerListViewHolder>(
    BusinessSideDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerListViewHolder {
        val binding =
            ItemBusinessSideListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerListViewHolder, position: Int) {
        val customer = getItem(position)
        holder.bind(customer)
    }

    fun setData(data: List<BusinessSideEntity>) {
        submitList(data)
    }

    inner class CustomerListViewHolder(private val binding: ItemBusinessSideListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusinessSideEntity) = with(binding) {

            tvCode.text = item.code
            tvName.text = item.name
            tvPhone.text = item.phone
            tvMobile.text = item.mobile
            tvAddress.text = item.address
            tvNationalOrEconomicCode.text = item.nationalOrEconomicCode
            tvPersonType.text = if (item.personType == PersonType.REAL) "حقیقی" else "حقوقی"

            ivEdit.setOnClickListener { onEdit(item) }

            ivDelete.setOnClickListener {
                onDelete(item)
            }

        }
    }
}

class BusinessSideDiffCallback : DiffUtil.ItemCallback<BusinessSideEntity>() {
    override fun areItemsTheSame(
        oldItem: BusinessSideEntity,
        newItem: BusinessSideEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BusinessSideEntity,
        newItem: BusinessSideEntity
    ): Boolean {
        return oldItem == newItem
    }
}