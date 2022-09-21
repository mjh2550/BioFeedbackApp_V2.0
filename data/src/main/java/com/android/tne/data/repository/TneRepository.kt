package com.android.tne.data.repository

import android.content.Context
import com.android.tne.data.datasource.TneLocalDataSource
import com.android.tne.data.model.entity.BioEntity
import com.android.tne.data.network.ApiClient
import com.android.tne.domain.model.Bio
import com.android.tne.domain.model.Test
import com.android.tne.domain.repository.ITneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject


class TneRepository @Inject constructor(
    //Db datasource
    private val localDataSource: TneLocalDataSource,
) : ITneRepository {
    private var api : ApiClient = ApiClient.getClient()
    override fun testStr() = "testStr"
    override fun getAllList() = api.getAllList()
    override fun getSelectBioList(): Flow<List<Bio>> = localDataSource.getSelectBioList()
}