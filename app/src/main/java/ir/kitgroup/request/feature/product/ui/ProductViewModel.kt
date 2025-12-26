package ir.kitgroup.request.feature.product.ui

import androidx.lifecycle.*
import ir.kitgroup.request.feature.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.request.core.database.dao.FeatureHistoryDao
import ir.kitgroup.request.core.database.entity.FeatureHistoryEntity
import ir.kitgroup.request.core.database.entity.ProductEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val featureHistoryDao: FeatureHistoryDao,
) : ViewModel() {

    val businessSideList: LiveData<List<ProductEntity>> = productRepository.getAll().asLiveData()

    fun insert(entity: ProductEntity) =
        viewModelScope.launch { productRepository.insert(entity) }

    fun update(entity: ProductEntity) =
        viewModelScope.launch { productRepository.update(entity) }

    fun delete(entity: ProductEntity) =
        viewModelScope.launch { productRepository.delete(entity) }

    suspend fun getAllFeatures(): List<String> {
        return featureHistoryDao.getAllFeatures()
    }

    fun saveFeatureHistory(features: List<String>) {
        viewModelScope.launch {
            features.forEach {
                if (it.isNotBlank()) {
                    featureHistoryDao.insert(FeatureHistoryEntity(value = it))
                }
            }
        }
    }
}

