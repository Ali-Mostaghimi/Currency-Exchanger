package com.github.alimostaghimi.currencyexchanger.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import com.github.alimostaghimi.currencyexchanger.domain.model.TransactionResult
import com.github.alimostaghimi.currencyexchanger.domain.usecase.BalanceUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.GetRatesUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.GetTransactionCountUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.TransactionUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionWithCountOfTransactionRequest
import com.github.alimostaghimi.currencyexchanger.utils.common.Event
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val getRatesUsecase: GetRatesUsecase,
    private val transactionUsecase: TransactionUsecase,
    private val getTransactionCountUsecase: GetTransactionCountUsecase,
    private val balanceUsecase: BalanceUsecase
) : ViewModel() {

    private val _rates: MutableStateFlow<BaseResponse<RatesWrapper>> =
        MutableStateFlow(BaseResponse.Loading)
    val rates: StateFlow<BaseResponse<RatesWrapper>> = _rates
    var ratesJob: Job = Job()

    private val _transactionResult: MutableStateFlow<Event<TransactionResult?>> = MutableStateFlow(Event(null))
    val transactionResult: MutableStateFlow<Event<TransactionResult?>> = _transactionResult

    val balance: Flow<List<Currency>> =
        balanceUsecase
            .getBalance()
            .distinctUntilChanged()

    fun getRates() {
        if (ratesJob.isActive) {
            ratesJob.cancel()
        }
        ratesJob = viewModelScope.launch {
            getRatesUsecase.getRates()
                .collect {
                    _rates.value = it
                }
        }
    }

    fun stopGettingRates() {
        ratesJob.cancel()
    }

    fun calculateTransactionForPreview(
        sourceUnit: String?,
        destinationUnit: String?,
        transactionAmount: Double?
    ): Double? {
        return if (transactionAmount == null || sourceUnit == null || destinationUnit == null) {
            null
        } else {
            transactionUsecase.calculateTransactionForPreview(
                lastRatesResponse = rates.value,
                sourceUnit = sourceUnit,
                destinationUnit = destinationUnit,
                transactionAmount = transactionAmount
            )
        }
    }

    fun exchange(
        sourceUnit: String?,
        destinationUnit: String?,
        transactionAmount: Double?
    ) {
        viewModelScope.launch {
            val result = transactionUsecase.exchange(
                lastRatesResponse = rates.value,
                sourceUnit = sourceUnit,
                destinationUnit = destinationUnit,
                commissionRequest = object : CommissionWithCountOfTransactionRequest {
                    override var transactionCountSinceNow: Int
                        get() = getTransactionCountUsecase.getTransactionCount()
                        set(value) {}
                },
                transactionAmount = transactionAmount
            )

            transactionResult.value = Event(result)
        }
    }
}