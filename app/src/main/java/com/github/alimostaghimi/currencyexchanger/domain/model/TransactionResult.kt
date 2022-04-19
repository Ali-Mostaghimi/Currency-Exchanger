package com.github.alimostaghimi.currencyexchanger.domain.model

sealed interface TransactionResult {
    class Success(val sourceAmount: Double, val destinationAmount: Double,val sourceUnit: String, val destinationUnit: String, val commissionFee: Double) : TransactionResult
    class Error(val transactionErrorState: TransactionErrorState) : TransactionResult
}
    
