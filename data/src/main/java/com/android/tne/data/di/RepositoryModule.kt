package com.android.tne.data.di

import android.content.Context
import com.android.tne.data.repository.TneRepository
import com.android.tne.domain.repository.ITneRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTneRepository (
        //필요시 db소스 input
    ) : ITneRepository {
        return TneRepository()
    }
}