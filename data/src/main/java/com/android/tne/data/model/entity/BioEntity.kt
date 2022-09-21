package com.android.tne.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bio_table")
data class BioEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bio_id") var bioId : Long?,
    @ColumnInfo(name = "bio_data") var bioData : String,
    @ColumnInfo(name = "data_type") var bioDataType : String,
    @ColumnInfo(name = "reg_time") var regTime : String
){

    constructor(bioData: String,bioDataType: String,regTime: String):this(
        null,
        bioData,
        bioDataType,
        regTime
    )
}