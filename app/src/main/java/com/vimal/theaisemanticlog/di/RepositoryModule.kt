package com.vimal.theaisemanticlog.di

import com.vimal.theaisemanticlog.data.repository.LogRepositoryImpl
import com.vimal.theaisemanticlog.domain.repository.LogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: LogRepositoryImpl):LogRepository

}