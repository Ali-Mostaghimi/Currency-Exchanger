package com.github.alimostaghimi.currencyexchanger.data.datasource.remote

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesApiService {

    @GET(LATEST_RATES_ENDPOINT)
    suspend fun getLatestRates(
        @Query(ACCESS_KEY_QUERY_PARAM_NAME) apiAccessKey: String
    ): ExchangeRatesResponse
}