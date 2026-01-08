package ir.kitgroup.request.feature.request.model

data class RequestHeaderDto(
    val id: Long,
    val orderGiverName: String,
    val orderReceiverName: String,
    val maxPrice: Long,
    val createDate: Long
)
