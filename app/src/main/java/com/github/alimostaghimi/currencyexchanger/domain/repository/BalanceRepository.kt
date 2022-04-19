package com.github.alimostaghimi.currencyexchanger.domain.repository

import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import kotlinx.coroutines.flow.Flow

interface BalanceRepository {

    fun getAllCurrencies(): Flow<List<Currency>>

    suspend fun insertCurrency(currency: Currency)

    suspend fun updateCurrency(currency: Currency)

    suspend fun updateCurrencies(vararg currency: Currency)

    suspend fun deleteCurrency(currency: Currency)

    suspend fun getCurrencyAmount(currencyUnit: String): Double?
}