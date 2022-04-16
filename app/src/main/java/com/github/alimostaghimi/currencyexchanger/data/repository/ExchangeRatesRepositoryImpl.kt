package com.github.alimostaghimi.currencyexchanger.data.repository

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesDataSource
import com.github.alimostaghimi.currencyexchanger.data.mapper.ExchangeRatesResponseMapper
import com.github.alimostaghimi.currencyexchanger.di.ExchangeRatesDataSourceType
import com.github.alimostaghimi.currencyexchanger.di.NamedExchangeRatesDataSource
import com.github.alimostaghimi.currencyexchanger.domain.error.ErrorHandler
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import com.github.alimostaghimi.currencyexchanger.domain.repository.ExchangeRatesRepository
import com.github.alimostaghimi.currencyexchanger.utils.extensions.toResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRatesRepositoryImpl @Inject constructor(
    @NamedExchangeRatesDataSource(ExchangeRatesDataSourceType.ActualImpl)
    private val exchangeRatesDataSource: ExchangeRatesDataSource,
    private val errorHandler: ErrorHandler
): ExchangeRatesRepository {

    override fun getExchangeRates(): Flow<BaseResponse<RatesWrapper>> =
        exchangeRatesDataSource.exchangeRatesFlow()
            .toResult(errorHandler = errorHandler, mapper = ExchangeRatesResponseMapper())
}