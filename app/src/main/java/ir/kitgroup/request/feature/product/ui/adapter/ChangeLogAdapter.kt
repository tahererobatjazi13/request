package ir.kitgroup.request.feature.product.ui.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kitgroup.request.R
import ir.kitgroup.request.core.database.entity.ProductChangeLog
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.core.formatDateShamsi
import ir.kitgroup.request.databinding.ItemChangeLogBinding
import java.text.DecimalFormat

class ChangeLogAdapter :
    ListAdapter<ProductChangeLog, ChangeLogAdapter.MaterialViewHolder>(ChangeLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding =
            ItemChangeLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaterialViewHolder(binding)
    }

    val priceFormatter: DecimalFormat = DecimalFormat("#,###,###,###")

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = getItem(position)
        holder.bind(material)
    }
    fun setData(data: List<ProductChangeLog>) {
        submitList(data)
    }
    inner class MaterialViewHolder(private val binding: ItemChangeLogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductChangeLog) {

            val context = itemView.context
            val attrRes =
                if (adapterPosition % 2 == 0) R.attr.colorBackGrayLight else R.attr.colorBackGrayDark

            binding.root.setBackgroundColor(TypedValue().apply {
                context.theme.resolveAttribute(attrRes, this, true)
            }.data)

            binding.tvChangeDate.text = formatDateShamsi(product.changeDate)
            binding.tvName.text = product.productName
            binding.tvOldPrice.text = priceFormatter.format(product.oldValue)
            binding.tvNewPrice.text = priceFormatter.format(product.newValue)
        }
    }
}

class ChangeLogDiffCallback : DiffUtil.ItemCallback<ProductChangeLog>() {
    override fun areItemsTheSame(oldItem: ProductChangeLog, newItem: ProductChangeLog): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductChangeLog,
        newItem: ProductChangeLog
    ): Boolean {
        return oldItem == newItem
    }
}
