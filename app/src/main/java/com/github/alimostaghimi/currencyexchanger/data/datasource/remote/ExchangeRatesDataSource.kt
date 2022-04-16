package com.github.alimostaghimi.currencyexchanger.data.datasource.remote

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesDataSource {
    fun exchangeRatesFlow(): Flow<ExchangeRatesResponse>
}