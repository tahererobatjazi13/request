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
    fun provideCustomerDao(db: AppDatabase): BusinessSideDao =
        db.businessSideDao()

}
