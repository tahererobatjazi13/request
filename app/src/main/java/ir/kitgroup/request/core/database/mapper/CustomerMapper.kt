package ir.kitgroup.request.core.database.mapper

import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import ir.kitgroup.request.core.utils.CustomerRole
import ir.kitgroup.request.core.utils.PersonType
import ir.kitgroup.request.feature.business_side.model.BusinessSideModel

object CustomerMapper {

    fun toEntity(
        code: String,
        name: String,
        address: String,
        phone: String,
        mobile: String,
        logoPath: String?,
        nationalCode: String,
        personType: PersonType,
        role: CustomerRole
    ): BusinessSideEntity {
        return BusinessSideEntity(
            code = code,
            name = name,
            address = address,
            phone = phone,
            mobile = mobile,
            logoPath = logoPath,
            nationalOrEconomicCode = nationalCode,
            personType = personType,
            customerRole = role
        )
    }

    fun toModel(entity: BusinessSideEntity): BusinessSideModel {
        return BusinessSideModel(
            id = entity.id,
            code = entity.code,
            name = entity.name,
            phone = entity.phone,
            mobile = entity.mobile,
            personType = if (entity.personType == PersonType.REAL) "حقیقی" else "حقوقی",
            role = if (entity.customerRole == CustomerRole.ORDER_GIVER) "سفارش دهنده" else "سفارش گیرنده"
        )
    }
}
