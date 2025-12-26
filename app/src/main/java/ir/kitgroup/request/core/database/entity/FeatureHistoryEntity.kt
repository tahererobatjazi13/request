package ir.kitgroup.request.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feature_history")
data class FeatureHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: String
)
