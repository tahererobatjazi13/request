package ir.kitgroup.request.feature.home.repository

import ir.kitgroup.request.core.database.dao.BusinessSideDao
import ir.kitgroup.request.core.database.dao.ProductDao
import ir.kitgroup.request.core.utils.CustomerRole
import ir.kitgroup.request.feature.home.model.RequestValidationResult
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val productDao: ProductDao,
    private val businessSideDao: BusinessSideDao
) {

    suspend fun hasProduct(): Boolean {
        return productDao.getCount() > 0
    }

    suspend fun hasOrderGiver(): Boolean {
        return businessSideDao.getCountByRole(CustomerRole.ORDER_GIVER) > 0
    }

    suspend fun hasOrderReceiver(): Boolean {
        return businessSideDao.getCountByRole(CustomerRole.ORDER_RECEIVER) > 0
    }

    suspend fun canCreateRequest(): RequestValidationResult {

        if (!hasProduct())
            return RequestValidationResult.NoProduct

        if (!hasOrderGiver())
            return RequestValidationResult.NoOrderGiver

        if (!hasOrderReceiver())
            return RequestValidationResult.NoOrderReceiver

        return RequestValidationResult.Ok
    }
}
