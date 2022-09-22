package com.android.tne.domain.di

import com.android.tne.domain.repository.ITneRepository
import com.android.tne.domain.usecase.FetchGetBioListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GetBioListUseCaseModule {

    @Provides
    @Singleton
    fun provideFetchGetBioList(
        tneRepository: ITneRepository
    ) : FetchGetBioListUseCase{
        return FetchGetBioListUseCase(tneRepository)
    }
}