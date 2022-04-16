package com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.transactioncount

interface TransactionCountPreferences {
    fun getTransactionCount(): Int
    fun setTransactionCount(transactionCount: Int)
    fun removeTransactionCount()
}