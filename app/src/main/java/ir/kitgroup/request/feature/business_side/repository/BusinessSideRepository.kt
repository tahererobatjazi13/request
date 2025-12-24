package ir.kitgroup.request.feature.business_side.repository

import ir.kitgroup.request.core.database.dao.BusinessSideDao
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import javax.inject.Inject


class BusinessSideRepository @Inject constructor(
    private val dao: BusinessSideDao
) {
    suspend fun insert(entity: BusinessSideEntity) = dao.insert(entity)
    suspend fun update(entity: BusinessSideEntity) = dao.update(entity)
    suspend fun delete(entity: BusinessSideEntity) = dao.delete(entity)

    fun getAll() = dao.getAll()
}
