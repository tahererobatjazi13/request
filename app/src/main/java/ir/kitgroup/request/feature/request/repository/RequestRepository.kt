package ir.kitgroup.request.feature.request.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import ir.kitgroup.request.core.database.dao.BusinessSideDao
import ir.kitgroup.request.core.database.dao.ProductDao
import ir.kitgroup.request.core.database.dao.RequestDao
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.core.database.entity.RequestHeaderEntity
import ir.kitgroup.request.feature.request.model.RequestHeaderDto
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val dao: ProductDao,
    private val businessSideDao: BusinessSideDao,
    private val requestDao: RequestDao,
) {

    suspend fun saveOrUpdateRequest(request: RequestHeaderEntity): Long {
        return if (request.id == 0L) {
            requestDao.insertRequestHeader(request)
        } else {
            requestDao.updateRequestHeader(request)
            request.id
        }
    }

    fun getRequestById(id: Int): LiveData<RequestHeaderEntity> {
        return requestDao.getRequestById(id)
    }
    fun getRequestHeaders(): LiveData<List<RequestHeaderDto>> {
        return requestDao.getRequestHeaders()
    }

    fun getAll() = businessSideDao.getAll()

    suspend fun insert(entity: ProductEntity) =
        dao.insert(entity)

    suspend fun update(entity: ProductEntity) =
        dao.update(entity)

    suspend fun delete(id: Long) {
        requestDao.deleteById(id)
    }


}
