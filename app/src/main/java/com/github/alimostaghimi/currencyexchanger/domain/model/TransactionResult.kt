package com.github.alimostaghimi.currencyexchanger.domain.model

sealed interface TransactionResult {
    class Success(commissionPercent: Float) : TransactionResult
    class Error(transactionErrorState: TransactionErrorState) : TransactionResult
}
    
