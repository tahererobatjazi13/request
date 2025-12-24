package ir.kitgroup.request.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kitgroup.request.core.database.dao.BusinessSideDao
import ir.kitgroup.request.core.database.entity.BusinessSideEntity

@Database(
    entities = [BusinessSideEntity::class],
    version = 1,
    exportSchema = false
)


abstract class AppDatabase : RoomDatabase() {
    abstract fun businessSideDao(): BusinessSideDao

}

