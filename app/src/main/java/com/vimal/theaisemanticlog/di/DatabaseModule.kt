package com.vimal.theaisemanticlog.di

import android.content.Context
import androidx.room.Room
import com.vimal.theaisemanticlog.data.local.AppDatabase
import com.vimal.theaisemanticlog.data.local.DatabaseConstants.DATABASE_NAME
import com.vimal.theaisemanticlog.data.local.dao.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ):AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideLogDao(
        db:AppDatabase
    ):LogDao {
        return db.logDao()
    }

}