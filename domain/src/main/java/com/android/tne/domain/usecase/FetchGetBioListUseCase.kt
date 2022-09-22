package com.android.tne.domain.usecase

import com.android.tne.domain.repository.ITneRepository
import javax.inject.Inject

class FetchGetBioListUseCase @Inject constructor(
    private val tneRepository: ITneRepository
) {
    operator fun invoke() = tneRepository.getSelectBioList()

}