package com.github.alimostaghimi.currencyexchanger.domain.model

enum class TransactionErrorState {
    RatesAreNotUpToDated,
    BalanceIsNotEnough,
    CanNotConvertSameCurrencies,
    SourceUnitIsNotSelected,
    DestinationUnitIsNotSelected,
    AmountOfTransactionIsNotSpecified

}