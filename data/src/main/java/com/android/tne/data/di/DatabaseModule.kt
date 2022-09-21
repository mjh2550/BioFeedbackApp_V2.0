package com.android.tne.data.di

import android.content.Context
import androidx.room.Room
import com.android.tne.data.db.TneDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTneDatabase(@ApplicationContext context : Context) : TneDatabase {
        return Room.databaseBuilder(
            context, TneDatabase :: class.java,"bio_db"
        ).build()
    }
}