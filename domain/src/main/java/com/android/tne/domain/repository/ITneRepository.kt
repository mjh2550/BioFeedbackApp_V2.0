package com.android.tne.domain.repository

import android.content.Context
import com.android.tne.domain.model.Bio
import com.android.tne.domain.model.Test
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface ITneRepository {
    fun testStr() : String
    fun getAllList() : Call<ArrayList<Test>>
    fun getSelectBioList() : Flow<List<Bio>>
}