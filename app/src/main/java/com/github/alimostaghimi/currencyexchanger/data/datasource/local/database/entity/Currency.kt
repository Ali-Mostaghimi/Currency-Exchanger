package com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.Balance_Table_Name


@Entity(tableName = Balance_Table_Name)
class Currency(

    @PrimaryKey
    @ColumnInfo(name = "currencyUnit")
    val currencyUnit: String,

    @ColumnInfo(name = "amount")
    val amount: Double
)