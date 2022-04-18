package com.github.alimostaghimi.currencyexchanger.data.datasource.remote

import com.github.alimostaghimi.currencyexchanger.BuildConfig
import com.github.alimostaghimi.currencyexchanger.data.datasource.DataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRatesDataSourceImpl @Inject constructor(
    private val ratesDataSynchronizer: DataSynchronizer<Any, ExchangeRatesResponse>,
    private val exchangeRatesApiService: ExchangeRatesApiService
) : ExchangeRatesDataSource {

    override fun exchangeRatesFlow(): Flow<ExchangeRatesResponse> =
        ratesDataSynchronizer.syncData {
            exchangeRatesApiService.getLatestRates(BuildConfig.API_ACCESS_KEY)
        }
}