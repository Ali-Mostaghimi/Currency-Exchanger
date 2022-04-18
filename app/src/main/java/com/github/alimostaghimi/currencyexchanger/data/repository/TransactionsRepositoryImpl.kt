package com.github.alimostaghimi.currencyexchanger.data.repository

import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.transactioncount.TransactionCountPreferences
import com.github.alimostaghimi.currencyexchanger.domain.repository.TransactionsRepository
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(private val transactionCountPref: TransactionCountPreferences) : TransactionsRepository {

    override fun setTransactionCount(transactionCount: Int) {
        transactionCountPref.setTransactionCount(transactionCount = transactionCount)
    }

    override fun getTransactionCount(): Int = transactionCountPref.getTransactionCount()

    override fun increaseTransactionCount() {
        val previousTransactionCount = transactionCountPref.getTransactionCount()
        transactionCountPref.setTransactionCount(previousTransactionCount + 1)
    }
}