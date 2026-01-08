package ir.kitgroup.request.feature.request.ui

import androidx.lifecycle.*
import ir.kitgroup.request.feature.request.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.core.database.entity.RequestHeaderEntity
import ir.kitgroup.request.feature.request.model.RequestHeaderDto
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository,
) : ViewModel() {
    val businessSideList: LiveData<List<BusinessSideEntity>> =
        requestRepository.getAll().asLiveData()

    //  val productList: LiveData<List<ProductEntity>> = requestRepository.getAll().asLiveData()

    fun insert(entity: ProductEntity) =
        viewModelScope.launch { requestRepository.insert(entity) }

    fun update(entity: ProductEntity) =
        viewModelScope.launch { requestRepository.update(entity) }

    fun delete(id: Long) =
        viewModelScope.launch { requestRepository.delete(id) }

    fun getRequestById(id: Int): LiveData<RequestHeaderEntity> {
        return requestRepository.getRequestById(id)
    }

    // انتخاب‌ها از Spinner
    private var orderGiverId: Long? = null
    private var orderReceiverId: Long? = null

    fun setOrderGiver(id: Long) {
        orderGiverId = id
    }

    fun setOrderReceiver(id: Long) {
        orderReceiverId = id
    }

    fun saveRequest(
        id: Long = 0,
        maxCapacity1: Int,
        maxCapacity2: Int,
        maxPrice: Long,
        description: String
    ) {
        val giverId = orderGiverId ?: return
        val receiverId = orderReceiverId ?: return

        viewModelScope.launch {
            val request = RequestHeaderEntity(
                id = id, // اگه صفر باشه اضافه میشه، اگه غیر صفر باشه آپدیت میشه
                orderGiverId = giverId,
                orderReceiverId = receiverId,
                maxCapacity1 = maxCapacity1,
                maxCapacity2 = maxCapacity2,
                maxPrice = maxPrice,
                description = description
            )
            requestRepository.saveOrUpdateRequest(request)
        }
    }


    // ---------- لیست هدر درخواست‌ها ----------
    val requestHeaders: LiveData<List<RequestHeaderDto>> =
        requestRepository.getRequestHeaders()
}

