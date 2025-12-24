package ir.kitgroup.request.feature.business_side.model

data class BusinessSideModel(
    val id: Long,
    val code: String,
    val name: String,
    val phone: String,
    val mobile: String,
    val personType: String,
    val role: String
)
