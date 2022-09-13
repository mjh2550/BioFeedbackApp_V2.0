package com.android.tne.domain.usecase

import com.android.tne.domain.repository.ITneRepository
import javax.inject.Inject

class FetchBoardGetAllListUseCase @Inject constructor(
    private val repository: ITneRepository
) {
    operator fun invoke() = repository.getAllList()
}