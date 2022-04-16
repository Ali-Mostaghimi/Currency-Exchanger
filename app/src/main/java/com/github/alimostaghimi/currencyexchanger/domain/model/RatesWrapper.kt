package com.github.alimostaghimi.currencyexchanger.domain.model

data class RatesWrapper(
    val rates: Map<String, Double>,
    val lastUpdateTimestamp: Long
)