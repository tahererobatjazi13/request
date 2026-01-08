package ir.kitgroup.request.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request_header")
data class RequestHeaderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderGiverId: Long,
    val orderReceiverId: Long,
    val maxCapacity1: Int,
    val maxCapacity2: Int,
    val maxPrice: Long,
    val description: String,
    val createDate: Long = System.currentTimeMillis()
)
