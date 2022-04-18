package com.github.alimostaghimi.currencyexchanger.domain.usecase

import com.github.alimostaghimi.currencyexchanger.di.ExchangeRatesRepositoryType
import com.github.alimostaghimi.currencyexchanger.di.NamedExchangeRatesRepository
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import com.github.alimostaghimi.currencyexchanger.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

class GetRatesUsecase @Inject constructor(
    @NamedExchangeRatesRepository(ExchangeRatesRepositoryType.ActualImpl)
    private val rateExchangeRatesRepository: ExchangeRatesRepository
) {
    var lastRatesResponse: BaseResponse<RatesWrapper>? = null
        private set

    fun getRates(): Flow<BaseResponse<RatesWrapper>> =
        rateExchangeRatesRepository.getExchangeRates()
            .onEach {
                lastRatesResponse = it
            }
}