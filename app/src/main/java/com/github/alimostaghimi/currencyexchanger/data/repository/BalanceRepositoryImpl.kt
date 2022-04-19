package com.github.alimostaghimi.currencyexchanger.data.repository

import androidx.room.withTransaction
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.DatabaseService
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.dao.BalanceDao
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.domain.repository.BalanceRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(private val databaseService: DatabaseService) :
    BalanceRepository {

    private val balanceDao: BalanceDao = databaseService.balanceDao()

    override fun getAllCurrencies(): Flow<List<Currency>> =
        balanceDao.getAllCurrencies()

    override suspend fun insertCurrency(currency: Currency) =
        balanceDao.insertCurrency(currency)

    override suspend fun updateCurrency(currency: Currency) {
        val amount = balanceDao.getCurrencyAmount(currency.currencyUnit)
        if (amount == null) {
            balanceDao.insertCurrency(currency)
        } else {
            balanceDao.updateCurrency(currency)
        }
    }

    override suspend fun updateCurrencies(vararg currency: Currency) {
        databaseService.withTransaction {
            for (c in currency) {
                balanceDao.insertCurrency(c)
            }
        }
    }

    override suspend fun deleteCurrency(currency: Currency) =
        balanceDao.deleteCurrency(currency)

    override suspend fun getCurrencyAmount(currencyUnit: String): Double? =
        balanceDao.getCurrencyAmount(currencyUnit)
}