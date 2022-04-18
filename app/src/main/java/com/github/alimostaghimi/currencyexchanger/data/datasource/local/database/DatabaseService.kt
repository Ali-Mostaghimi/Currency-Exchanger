package com.github.alimostaghimi.currencyexchanger.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.dao.BalanceDao
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import javax.inject.Singleton


@Singleton
@Database(
    entities = [Currency::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseService : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}