package com.github.alimostaghimi.currencyexchanger.domain.repository

import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import kotlinx.coroutines.flow.Flow

interface BalanceRepository {

    fun getAllCurrencies(): Flow<List<Currency>>

    fun insertCurrency(currency: Currency)

    fun updateCurrency(currency: Currency)

    fun updateCurrencies(vararg currency: Currency)

    fun deleteCurrency(currency: Currency)

    fun getCurrencyAmount(currencyUnit: String): Double
}