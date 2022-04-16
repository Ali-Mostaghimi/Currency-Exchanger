package com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.transactioncount

import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base.PrefStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionCountPreferencesImpl @Inject constructor(private val prefStorage: PrefStorage) : TransactionCountPreferences {

    companion object{
        const val KEY_TRANSACTION_COUNT = "TRANSACTION_COUNT"
    }

    override fun getTransactionCount(): Int =
        prefStorage.getString(KEY_TRANSACTION_COUNT, "0")?.toInt() ?: 0

    override fun setTransactionCount(transactionCount: Int) =
        prefStorage.setString(KEY_TRANSACTION_COUNT, transactionCount.toString())

    override fun removeTransactionCount() =
        prefStorage.removeString(KEY_TRANSACTION_COUNT)
}