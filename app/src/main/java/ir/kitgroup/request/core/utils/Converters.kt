package ir.kitgroup.request.core.utils

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromPersonType(value: PersonType): String = value.name

    @TypeConverter
    fun toPersonType(value: String): PersonType =
        PersonType.valueOf(value)

    @TypeConverter
    fun fromCustomerRole(value: CustomerRole): String = value.name

    @TypeConverter
    fun toCustomerRole(value: String): CustomerRole =
        CustomerRole.valueOf(value)
}
