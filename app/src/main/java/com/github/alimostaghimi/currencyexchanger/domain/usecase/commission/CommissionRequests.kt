package com.github.alimostaghimi.currencyexchanger.domain.usecase.commission

interface CommissionRequest
interface CommissionWithCountOfTransactionRequest : CommissionRequest {
    var transactionCountSinceNow: Int
}

interface CommissionWithAmountOfTransactionRequest : CommissionRequest {
    var amountOfTransactionInEuro: Int
}

interface CommissionWithAmountAndTransactionContRequest:
    CommissionWithCountOfTransactionRequest, CommissionWithAmountOfTransactionRequest