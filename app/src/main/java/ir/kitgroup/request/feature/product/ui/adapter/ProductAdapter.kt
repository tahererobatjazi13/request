package ir.kitgroup.request.feature.product.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.databinding.ItemProductListBinding
import java.text.DecimalFormat

class ProductAdapter(
    private val onChangeLog: (ProductEntity) -> Unit = {},
    private val onEdit: (ProductEntity) -> Unit,
    private val onDelete: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, ProductAdapter.ProductListViewHolder>(
    ProductDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val binding =
            ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListViewHolder(binding)
    }

    private val formatter = DecimalFormat("#,###")

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val customer = getItem(position)
        holder.bind(customer)
    }

    fun setData(data: List<ProductEntity>) {
        submitList(data)
    }

    inner class ProductListViewHolder(private val binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) = with(binding) {

            tvCode.text = item.code
            tvName.text = item.name
            tvFeature1.text = item.feature1
            tvFeature2.text = item.feature2
            tvFeature3.text = item.feature3
            tvFeature4.text = item.feature4
            tvUnit.text = formatter.format(item.unit)
            tvUnitName.text = item.unitName
            tvPrice.text = formatter.format(item.price) + " ریال"

            ivHistory.setOnClickListener { onChangeLog(item) }
            ivEdit.setOnClickListener { onEdit(item) }

            ivDelete.setOnClickListener {
                onDelete(item)
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
    override fun areItemsTheSame(
        oldItem: ProductEntity,
        newItem: ProductEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductEntity,
        newItem: ProductEntity
    ): Boolean {
        return oldItem == newItem
    }
}