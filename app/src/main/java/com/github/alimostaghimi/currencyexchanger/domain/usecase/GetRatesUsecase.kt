package com.github.alimostaghimi.currencyexchanger.domain.usecase

import com.github.alimostaghimi.currencyexchanger.di.ExchangeRatesRepositoryType
import com.github.alimostaghimi.currencyexchanger.di.NamedExchangeRatesRepository
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import com.github.alimostaghimi.currencyexchanger.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRatesUsecase @Inject constructor(
    @NamedExchangeRatesRepository(ExchangeRatesRepositoryType.ActualImpl)
    private val rateExchangeRatesRepository: ExchangeRatesRepository
) {
    fun getRates(): Flow<BaseResponse<RatesWrapper>> =
        rateExchangeRatesRepository.getExchangeRates()
}