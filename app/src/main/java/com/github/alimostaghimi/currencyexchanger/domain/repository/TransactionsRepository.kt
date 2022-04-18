package com.github.alimostaghimi.currencyexchanger.domain.repository

interface TransactionsRepository {
    fun setTransactionCount(transactionCount: Int)
    fun getTransactionCount(): Int
    fun increaseTransactionCount()
}