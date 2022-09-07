package com.android.tne.domain.usecase

import android.content.Context
import com.android.tne.domain.repository.ITneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FetchTestStrUseCase @Inject constructor(
    private val tneRepository: ITneRepository,
) {
    /**
     * 연산자 오버로딩
     */
    operator fun invoke() = tneRepository.testStr()
}