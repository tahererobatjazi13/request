package ir.kitgroup.request.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.kitgroup.request.core.database.entity.FeatureHistoryEntity

@Dao
interface FeatureHistoryDao {

    @Query("""
    SELECT DISTINCT value 
    FROM feature_history 
    WHERE featureType = :type
""")
    suspend fun getFeaturesByType(type: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(feature: FeatureHistoryEntity)
}
