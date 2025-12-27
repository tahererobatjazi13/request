package ir.kitgroup.request.core.database.module

import android.content.Context
import androidx.room.Room
import ir.kitgroup.request.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.kitgroup.request.core.database.dao.BusinessSideDao
import ir.kitgroup.request.core.database.dao.FeatureHistoryDao
import ir.kitgroup.request.core.database.dao.ProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "request_db")
            .build()

    @Provides
    fun provideBusinessSideDao(db: AppDatabase): BusinessSideDao =
        db.businessSideDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao =
        db.productDao()

    @Provides
    fun provideFeatureHistoryDao(db: AppDatabase): FeatureHistoryDao =
        db.featureHistoryDao()

}
