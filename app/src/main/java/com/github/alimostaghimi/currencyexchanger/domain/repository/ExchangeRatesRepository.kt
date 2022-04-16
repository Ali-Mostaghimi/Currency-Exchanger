package com.github.alimostaghimi.currencyexchanger.domain.repository

import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {
    fun getExchangeRates(): Flow<BaseResponse<RatesWrapper>>
}