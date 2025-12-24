package ir.kitgroup.request.feature.product.model

data class ProductModel(
    val id: Long,
    val code: String,
    val name: String,
    val phone: String,
    val mobile: String,
    val personType: String,
    val role: String
)
