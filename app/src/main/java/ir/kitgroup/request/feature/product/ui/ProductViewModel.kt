package ir.kitgroup.request.feature.product.ui

import androidx.lifecycle.*
import ir.kitgroup.request.feature.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    val businessSideList: LiveData<List<BusinessSideEntity>> = repository.getAll().asLiveData()

    fun insert(entity: BusinessSideEntity) =
        viewModelScope.launch { repository.insert(entity) }

    fun update(entity: BusinessSideEntity) =
        viewModelScope.launch { repository.update(entity) }

    fun delete(entity: BusinessSideEntity) =
        viewModelScope.launch { repository.delete(entity) }
}

