package ir.kitgroup.request.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
}
