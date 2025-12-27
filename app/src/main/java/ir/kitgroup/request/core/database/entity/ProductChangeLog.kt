package ir.kitgroup.request.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_change_logs")
data class ProductChangeLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val productName: String,
    val changeDate: Long,    // تاریخ تغییر (System.currentTimeMillis())
    val changeType: Int,     // نوع تغییر (مثلاً 1 برای تغییر قیمت، 2 برای تغییر نام و غیره)
    val oldValue: Double,    // قیمت قبلی
    val newValue: Double     // قیمت جدید
)
