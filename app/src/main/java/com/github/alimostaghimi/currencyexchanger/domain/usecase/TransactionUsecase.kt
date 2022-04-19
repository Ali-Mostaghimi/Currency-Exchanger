package com.github.alimostaghimi.currencyexchanger.domain.usecase

import com.github.alimostaghimi.currencyexchanger.data.datasource.ExchangeRates_Syncing_Period
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper
import com.github.alimostaghimi.currencyexchanger.domain.model.TransactionErrorState
import com.github.alimostaghimi.currencyexchanger.domain.model.TransactionResult
import com.github.alimostaghimi.currencyexchanger.domain.repository.BalanceRepository
import com.github.alimostaghimi.currencyexchanger.domain.repository.TransactionsRepository
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionCalculator
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionRequest
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class TransactionUsecase @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val transactionsRepository: TransactionsRepository,
    private val commissionCalculator: CommissionCalculator
) {
    suspend fun exchange(
        sourceUnit: String?,
        destinationUnit: String?,
        transactionAmount: Double?,
        commissionRequest: CommissionRequest,
        lastRatesResponse: BaseResponse<RatesWrapper>
    ): TransactionResult {

        if (sourceUnit == null) {
            return TransactionResult.Error(transactionErrorState = TransactionErrorState.SourceUnitIsNotSelected)
        }
        if (destinationUnit == null) {
            return TransactionResult.Error(transactionErrorState = TransactionErrorState.DestinationUnitIsNotSelected)

        }
        if (transactionAmount == null) {
            return TransactionResult.Error(transactionErrorState = TransactionErrorState.AmountOfTransactionIsNotSpecified)
        }

        if (sourceUnit == destinationUnit){
            return TransactionResult.Error(transactionErrorState = TransactionErrorState.CanNotConvertSameCurrencies)
        }

        val rates: Map<String, Double> =
            if (lastRatesResponse is BaseResponse.Success && (Date().time - lastRatesResponse.data.lastUpdateTimestamp) < ExchangeRates_Syncing_Period) {
                lastRatesResponse.data.rates
            } else {
                // rates are not up to data or state is not success
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
            ?: return TransactionResult.Error(transactionErrorState = TransactionErrorState.BalanceIsNotEnough)
        //sourceAmount after decreasing transactionAmount and commission
        val sourceAmountAfterTransaction =
            sourceAmount - transactionAmount - (transactionAmount * commission)

        if (sourceAmountAfterTransaction <= 0) {
            return TransactionResult.Error(transactionErrorState = TransactionErrorState.BalanceIsNotEnough)
        }

        // destination currency updating
        val destinationAmount = balanceRepository.getCurrencyAmount(destinationUnit)
        val destinationCurrencyUpdate = if (destinationAmount == null) {
            Currency(destinationUnit, transactionAmount * ratio)
        } else {
            Currency(destinationUnit, destinationAmount + transactionAmount * ratio)
        }

        // transaction can be apply and updating currencies in db
        balanceRepository.updateCurrencies(
            Currency(sourceUnit, sourceAmountAfterTransaction),
            destinationCurrencyUpdate
        )
        transactionsRepository.increaseTransactionCount()
        return TransactionResult.Success(
            sourceAmount = transactionAmount,
            destinationAmount = transactionAmount * ratio,
            sourceUnit = sourceUnit,
            destinationUnit = destinationUnit,
            commissionFee = transactionAmount * commission
        )
    }

    fun calculateTransactionForPreview(
        sourceUnit: String,
        destinationUnit: String,
        transactionAmount: Double,
        lastRatesResponse: BaseResponse<RatesWrapper>
    ): Double? {

        val rates: Map<String, Double> =
            if (lastRatesResponse is BaseResponse.Success) {
                lastRatesResponse.data.rates
            } else {
                return null
            }

        val sourceRate = rates.get(sourceUnit)
        val destinationRate = rates.get(destinationUnit)
        val ratio = if (destinationRate != null && sourceRate != null) {
            destinationRate / sourceRate
        } else {
            // not found source or destination in rates
            return null
        }

        return transactionAmount * ratio
    }
}