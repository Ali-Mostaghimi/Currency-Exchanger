package com.github.alimostaghimi.currencyexchanger.data.datasource

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ExchangeRates_Syncing_Period = 5 * 1000L // 5 second

class ExchangeRatesDataSynchronizer (
    private val period: Long = ExchangeRates_Syncing_Period,
    private val dispatcher: CoroutineDispatcher
) : DataSynchronizer<Any, ExchangeRatesResponse> {


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun syncData(dataFetcher: suspend (request: Any?) -> ExchangeRatesResponse): Flow<ExchangeRatesResponse> =
        channelFlow {
            while (!isClosedForSend){
                send(dataFetcher.invoke(null))
                delay(period)
            }

        }.flowOn(dispatcher)

}