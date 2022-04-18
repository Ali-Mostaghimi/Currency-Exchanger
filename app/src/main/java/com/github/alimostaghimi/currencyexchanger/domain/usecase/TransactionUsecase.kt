package com.github.alimostaghimi.currencyexchanger.domain.usecase

import com.github.alimostaghimi.currencyexchanger.data.datasource.ExchangeRates_Syncing_Period
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.TransactionErrorState
import com.github.alimostaghimi.currencyexchanger.domain.model.TransactionResult
import com.github.alimostaghimi.currencyexchanger.domain.repository.BalanceRepository
import com.github.alimostaghimi.currencyexchanger.domain.repository.TransactionsRepository
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionCalculator
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionRequest
import java.util.*
import javax.inject.Inject

class TransactionUsecase @Inject constructor(
    private val ratesUsecase: GetRatesUsecase,
    private val balanceRepository: BalanceRepository,
    private val transactionsRepository: TransactionsRepository,
    private val commissionCalculator: CommissionCalculator
) {
    fun exchange(
        sourceUnit: String,
        destinationUnit: String,
        transactionAmount: Double,
        commissionRequest: CommissionRequest
    ): TransactionResult {
        val lastRatesResponse = ratesUsecase.lastRatesResponse

        val rates: Map<String, Double> =
            if (lastRatesResponse is BaseResponse.Success && (Date().time - lastRatesResponse.data.lastUpdateTimestamp) < ExchangeRates_Syncing_Period) {
                lastRatesResponse.data.rates
            } else {
                // all the other states will treat as error
                return TransactionResult.Error(transactionErrorState = TransactionErrorState.RatesAreNotUpToDated)
            }

        val sourceRate = rates.get(sourceUnit)
        val destinationRate = rates.get(destinationUnit)
        val ratio = if (destinationRate != null && sourceRate != null) {
            destinationRate / sourceRate
        } else {
            // not found source or destination in rates
            return TransactionResult.Error(transactionErrorState = TransactionErrorState.RatesAreNotUpToDated)
        }

        val commission = commissionCalculator.calculateCommission(commissionRequest)


        val sourceAmount = balanceRepository.getCurrencyAmount(sourceUnit)
        //sourceAmount after decreasing transactionAmount and commission
        val sourceAmountAfterTransaction =
            sourceAmount - transactionAmount - (sourceAmount * commission)

        if (sourceAmountAfterTransaction <= 0) {
            return TransactionResult.Error(transactionErrorState = TransactionErrorState.BalanceIsNotEnough)
        }

        // transaction can be apply
        balanceRepository.updateCurrencies(
            Currency(sourceUnit, sourceAmountAfterTransaction),
            Currency(destinationUnit, sourceAmount * ratio)
        )
        transactionsRepository.increaseTransactionCount()
        return TransactionResult.Success(commissionPercent = commission)
    }
}