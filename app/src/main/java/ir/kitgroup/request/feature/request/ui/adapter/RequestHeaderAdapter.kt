package ir.kitgroup.request.feature.request.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kitgroup.request.databinding.ItemRequestHeaderListBinding
import ir.kitgroup.request.feature.request.model.RequestHeaderDto
import java.text.DecimalFormat

class RequestHeaderAdapter(
    private val onEdit: (RequestHeaderDto) -> Unit,
    private val onDelete: (RequestHeaderDto) -> Unit
) : ListAdapter<RequestHeaderDto, RequestHeaderAdapter.RequestHeaderViewHolder>(
    RequestHeaderDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHeaderViewHolder {
        val binding =
            ItemRequestHeaderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestHeaderViewHolder(binding)
    }

    private val formatter = DecimalFormat("#,###")

    override fun onBindViewHolder(holder: RequestHeaderViewHolder, position: Int) {
        val customer = getItem(position)
        holder.bind(customer)
    }

    fun setData(data: List<RequestHeaderDto>) {
        submitList(data)
    }

    inner class RequestHeaderViewHolder(private val binding: ItemRequestHeaderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RequestHeaderDto) = with(binding) {
            tvOrderGiver.text = item.orderGiverName
            tvOrderReceiver.text = item.orderReceiverName
            tvPrice.text = formatter.format(item.maxPrice) + " ریال"

            ivEdit.setOnClickListener { onEdit(item) }

            ivDelete.setOnClickListener {
                onDelete(item)
            }
        }
    }
}

class RequestHeaderDiffCallback : DiffUtil.ItemCallback<RequestHeaderDto>() {
    override fun areItemsTheSame(
        oldItem: RequestHeaderDto,
        newItem: RequestHeaderDto
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: RequestHeaderDto,
        newItem: RequestHeaderDto
    ): Boolean {
        return oldItem == newItem
    }
}