package ir.kitgroup.request.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.kitgroup.request.core.database.entity.RequestHeaderEntity
import ir.kitgroup.request.feature.request.model.RequestHeaderDto

@Dao
interface RequestDao {

    @Query("DELETE FROM request_header WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequestHeader(header: RequestHeaderEntity): Long

    @Update
    suspend fun updateRequestHeader(header: RequestHeaderEntity)

    @Query("SELECT * FROM request_header WHERE id = :id")
    fun getRequestById(id: Int): LiveData<RequestHeaderEntity>

    @Query(
        """
        SELECT r.id,
               og.name AS orderGiverName,
               orc.name AS orderReceiverName,
               r.maxPrice,
               r.createDate
        FROM request_header r
        JOIN customers og ON r.orderGiverId = og.id
        JOIN customers orc ON r.orderReceiverId = orc.id
        ORDER BY r.id DESC
    """
    )
    fun getRequestHeaders(): LiveData<List<RequestHeaderDto>>
}
