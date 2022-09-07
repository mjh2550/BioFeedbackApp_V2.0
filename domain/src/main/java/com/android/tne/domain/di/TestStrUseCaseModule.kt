package com.android.tne.domain.di

import com.android.tne.domain.repository.ITneRepository
import com.android.tne.domain.usecase.FetchTestStrUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TestStrUseCaseModule{

    @Provides
    @Singleton
    fun provideTestStr(
        tneRepository: ITneRepository
    ): FetchTestStrUseCase {
        return FetchTestStrUseCase(tneRepository = tneRepository)
    }

}