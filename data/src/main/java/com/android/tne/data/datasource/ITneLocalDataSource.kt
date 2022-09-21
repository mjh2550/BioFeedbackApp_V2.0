package com.android.tne.data.datasource

import com.android.tne.domain.model.Bio
import kotlinx.coroutines.flow.Flow

interface ITneLocalDataSource {

    //전체 데이터 조회
    fun getSelectBioList() : Flow<List<Bio>>
}