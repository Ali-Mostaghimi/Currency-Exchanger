package com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.dao

import androidx.room.*
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.Balance_Table_Name
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {

    @Query("SELECT * FROM balance")
    fun getAllCurrencies(): Flow<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Update
    suspend fun updateCurrency(currency: Currency)

    @Delete
    suspend fun deleteCurrency(currency: Currency)

    @Query("SELECT amount FROM $Balance_Table_Name WHERE currencyUnit = :currencyUnit LIMIT 1")
    suspend fun getCurrencyAmount(currencyUnit: String): Double?
}