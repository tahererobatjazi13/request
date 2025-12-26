package ir.kitgroup.request.feature.product.repository

import ir.kitgroup.request.core.database.dao.ProductDao
import ir.kitgroup.request.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val dao: ProductDao
) {
    suspend fun insert(entity: ProductEntity) =
        dao.insert(entity)

    suspend fun update(entity: ProductEntity) =
        dao.update(entity)

    suspend fun delete(entity: ProductEntity) =
        dao.delete(entity)

    fun getAll(): Flow<List<ProductEntity>> =
        dao.getAll()
}
