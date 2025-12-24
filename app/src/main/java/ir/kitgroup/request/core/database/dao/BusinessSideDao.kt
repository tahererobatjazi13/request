package ir.kitgroup.request.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessSideDao {
    @Insert
    suspend fun insert(entity: BusinessSideEntity)

    @Update
    suspend fun update(entity: BusinessSideEntity)

    @Delete
    suspend fun delete(entity: BusinessSideEntity)

    @Query("SELECT * FROM customers ORDER BY id DESC")
    fun getAll(): Flow<List<BusinessSideEntity>>
}
