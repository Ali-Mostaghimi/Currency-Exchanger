package com.github.alimostaghimi.currencyexchanger.presentation.home

import androidx.lifecycle.ViewModel
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import com.github.alimostaghimi.currencyexchanger.domain.usecase.BalanceUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.GetRatesUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.TransactionUsecase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val getRatesUsecase: GetRatesUsecase,
    private val transactionUsecase: TransactionUsecase,
    private val balanceUsecase: BalanceUsecase
) : ViewModel() {

    val rates: Flow<BaseResponse<RatesWrapper>> = getRatesUsecase.getRates()
    val balance: Flow<List<Currency>> =
        balanceUsecase
            .getBalance()
            .distinctUntilChanged()

}