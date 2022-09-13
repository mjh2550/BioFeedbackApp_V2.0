package com.android.tne.domain.repository

import android.content.Context
import com.android.tne.domain.model.Test
import retrofit2.Call

interface ITneRepository {
    fun testStr() : String
    fun getAllList() : Call<ArrayList<Test>>
}