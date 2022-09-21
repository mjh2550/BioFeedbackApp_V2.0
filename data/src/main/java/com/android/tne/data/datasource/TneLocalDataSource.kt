package com.android.tne.data.datasource

import com.android.tne.data.db.TneDatabase
import com.android.tne.domain.model.Bio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TneLocalDataSource @Inject constructor(
    private val db : TneDatabase
) : ITneLocalDataSource {
    override fun getSelectBioList(): Flow<List<Bio>> = db.bioDao().getSelectBioList()
}