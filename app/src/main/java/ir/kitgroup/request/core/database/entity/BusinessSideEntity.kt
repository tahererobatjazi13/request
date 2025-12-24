package ir.kitgroup.request.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.kitgroup.request.core.utils.CustomerRole
import ir.kitgroup.request.core.utils.PersonType

@Entity(tableName = "customers")
data class BusinessSideEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val name: String,
    val address: String,
    val phone: String,
    val mobile: String,
    val logoPath: String?, // مسیر عکس
    val nationalOrEconomicCode: String,

    val personType: PersonType,   // حقیقی / حقوقی
    val customerRole: CustomerRole // سفارش دهنده / سفارش گیرنده
)
