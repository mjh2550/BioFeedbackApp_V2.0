package com.android.tne.data.di

import com.android.tne.data.datasource.TneLocalDataSource
import com.android.tne.data.db.TneDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {
    @Provides
    @Singleton
    fun provideTneLocalDataSource(
        db : TneDatabase
    ) : TneLocalDataSource {
        return TneLocalDataSource(db)
    }
}