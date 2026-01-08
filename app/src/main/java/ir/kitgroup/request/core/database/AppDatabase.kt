package ir.kitgroup.request.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kitgroup.request.core.database.dao.BusinessSideDao
import ir.kitgroup.request.core.database.dao.FeatureHistoryDao
import ir.kitgroup.request.core.database.dao.ProductDao
import ir.kitgroup.request.core.database.dao.RequestDao
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import ir.kitgroup.request.core.database.entity.FeatureHistoryEntity
import ir.kitgroup.request.core.database.entity.ProductChangeLog
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.core.database.entity.RequestHeaderEntity

@Database(
    entities = [BusinessSideEntity::class, ProductEntity::class,
        FeatureHistoryEntity::class, ProductChangeLog::class, RequestHeaderEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun businessSideDao(): BusinessSideDao
    abstract fun productDao(): ProductDao
    abstract fun featureHistoryDao(): FeatureHistoryDao
    abstract fun requestDao(): RequestDao
}

