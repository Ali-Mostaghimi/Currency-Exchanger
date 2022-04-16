package com.github.alimostaghimi.currencyexchanger.presentation.home

import androidx.lifecycle.ViewModel
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import com.github.alimostaghimi.currencyexchanger.domain.usecase.GetRatesUsecase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val getRatesUsecase: GetRatesUsecase
) : ViewModel() {

    val rates: Flow<BaseResponse<RatesWrapper>> = getRatesUsecase.getRates()

}