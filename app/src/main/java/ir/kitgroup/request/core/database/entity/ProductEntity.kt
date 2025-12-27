package ir.kitgroup.request.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val name: String,
    val feature1: String?,
    val feature2: String?,
    val feature3: String?,
    val feature4: String?,
    val price: Double,
    val unitName: String, // نام واحد
    val unit: Double // ضریب واحد

)

