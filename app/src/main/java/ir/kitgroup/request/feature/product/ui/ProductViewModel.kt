package ir.kitgroup.request.feature.product.ui

import androidx.lifecycle.*
import ir.kitgroup.request.feature.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.request.core.database.dao.FeatureHistoryDao
import ir.kitgroup.request.core.database.entity.FeatureHistoryEntity
import ir.kitgroup.request.core.database.entity.ProductChangeLog
import ir.kitgroup.request.core.database.entity.ProductEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val featureHistoryDao: FeatureHistoryDao,
) : ViewModel() {

    val productList: LiveData<List<ProductEntity>> = productRepository.getAll().asLiveData()

    fun insert(entity: ProductEntity) =
        viewModelScope.launch { productRepository.insert(entity) }

    fun update(entity: ProductEntity) =
        viewModelScope.launch { productRepository.update(entity) }

    fun delete(entity: ProductEntity) =
        viewModelScope.launch { productRepository.delete(entity) }

    suspend fun getFeaturesByType(type: Int): List<String> {
        return featureHistoryDao.getFeaturesByType(type)
    }

    fun saveFeatureHistory(featureType: Int, value: String) {
        viewModelScope.launch {
            if (value.isNotBlank()) {
                featureHistoryDao.insert(
                    FeatureHistoryEntity(
                        featureType = featureType,
                        value = value
                    )
                )
            }
        }
    }


    fun updateProductWithPriceLog(
        oldProduct: ProductEntity,
        newProduct: ProductEntity
    ) {
        viewModelScope.launch {

            // اگر قیمت تغییر کرده
            if (oldProduct.price != newProduct.price) {

                val log = ProductChangeLog(
                    productId = oldProduct.id.toInt(),
                    productName = oldProduct.name,
                    changeDate = System.currentTimeMillis(),
                    changeType = 1, // 1 = تغییر قیمت
                    oldValue = oldProduct.price,
                    newValue = newProduct.price
                )

                productRepository.insertChangeLog(log)
            }

            productRepository.update(newProduct)
        }
    }

    fun getChangeLogsForProductByType(
        productId: Int,
        changeType: Int
    ): LiveData<List<ProductChangeLog>> {
        return productRepository.getChangeLogsForProductByType(productId, changeType)
    }
}

