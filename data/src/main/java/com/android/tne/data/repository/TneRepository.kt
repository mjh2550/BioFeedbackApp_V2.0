package com.android.tne.data.repository

import android.content.Context
import com.android.tne.domain.repository.ITneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class TneRepository @Inject constructor(
    //Db datasource
) : ITneRepository {
    override fun testStr() = "testStr"
}