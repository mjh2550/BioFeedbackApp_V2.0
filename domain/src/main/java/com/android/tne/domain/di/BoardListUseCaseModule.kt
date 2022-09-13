package com.android.tne.domain.di

import com.android.tne.domain.repository.ITneRepository
import com.android.tne.domain.usecase.FetchBoardGetAllListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class BoardListUseCaseModule {

    @Provides
    @Singleton
    fun provideFetchBoardListUseCase(
        repository: ITneRepository,
    ) : FetchBoardGetAllListUseCase{
        return FetchBoardGetAllListUseCase(
            repository = repository
        )
    }
}