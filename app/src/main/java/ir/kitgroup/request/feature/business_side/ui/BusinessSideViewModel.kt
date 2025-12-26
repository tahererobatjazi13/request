package ir.kitgroup.request.feature.business_side.ui

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import ir.kitgroup.request.feature.business_side.repository.BusinessSideRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessSideViewModel @Inject constructor(
    private val repository: BusinessSideRepository,
) : ViewModel() {

    val businessSideList: LiveData<List<BusinessSideEntity>> = repository.getAll().asLiveData()

    fun insert(entity: BusinessSideEntity) =
        viewModelScope.launch { repository.insert(entity) }

    fun update(entity: BusinessSideEntity) =
        viewModelScope.launch { repository.update(entity) }

    fun delete(entity: BusinessSideEntity) =
        viewModelScope.launch { repository.delete(entity) }
}

