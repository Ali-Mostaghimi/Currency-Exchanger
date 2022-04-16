package com.github.alimostaghimi.currencyexchanger.data.mapper

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import java.util.*

class ExchangeRatesResponseMapper: IMapper<ExchangeRatesResponse, RatesWrapper> {

    override fun map(data: ExchangeRatesResponse): RatesWrapper =
        RatesWrapper(
            rates = data.rates,
            lastUpdateTimestamp = Date().time
        )
}