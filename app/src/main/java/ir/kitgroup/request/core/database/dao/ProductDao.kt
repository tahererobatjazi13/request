package ir.kitgroup.request.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.kitgroup.request.core.database.entity.ProductChangeLog
import ir.kitgroup.request.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    suspend fun insert(product: ProductEntity)

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int

    @Insert
    suspend fun insertChangeLogs(log: ProductChangeLog)

    @Query(
        """
        SELECT * FROM product_change_logs
        WHERE productId = :productId AND changeType = :changeType
        ORDER BY changeDate DESC
    """
    )
    fun getLogsByProductAndType(
        productId: Int,
        changeType: Int
    ): LiveData<List<ProductChangeLog>>
}
