package com.github.alimostaghimi.currencyexchanger.data.datasource

import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import kotlinx.coroutines.flow.Flow

interface DataSynchronizer<I, O> {
    fun syncData(dataFetcher: suspend (request: I?) -> O): Flow<O>
}