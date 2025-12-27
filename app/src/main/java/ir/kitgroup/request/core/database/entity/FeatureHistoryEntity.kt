package ir.kitgroup.request.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "feature_history")
data class FeatureHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val featureType: Int, // 1,2,3,4
    val value: String
)
//1 = feature1
//2 = feature2
//3 = feature3
//4 = feature4