package com.android.tne.data.repository

import android.content.Context
import com.android.tne.data.network.ApiClient
import com.android.tne.domain.model.Test
import com.android.tne.domain.repository.ITneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import javax.inject.Inject


class TneRepository @Inject constructor(
    //Db datasource
) : ITneRepository {
    var api = ApiClient.getClient()
    override fun testStr() = "testStr"
    override fun getAllList() = api.getAllList()
}