package com.github.alimostaghimi.currencyexchanger.data.repository

import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.DatabaseService
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.dao.BalanceDao
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.domain.repository.BalanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(private val databaseService: DatabaseService) :
    BalanceRepository {

    private val balanceDao: BalanceDao = databaseService.balanceDao()

    override fun getAllCurrencies(): Flow<List<Currency>> =
        balanceDao.getAllCurrencies()

    override fun insertCurrency(currency: Currency) =
        balanceDao.insertCurrency(currency)

    override fun updateCurrency(currency: Currency) =
        balanceDao.updateCurrency(currency)

    override fun updateCurrencies(vararg currency: Currency) {
        databaseService.runInTransaction {
            for (c in currency)
            balanceDao.updateCurrency(c)
        }
    }

    override fun deleteCurrency(currency: Currency) =
        balanceDao.deleteCurrency(currency)

    override fun getCurrencyAmount(currencyUnit: String): Double =
        balanceDao.getCurrencyAmount(currencyUnit)
}