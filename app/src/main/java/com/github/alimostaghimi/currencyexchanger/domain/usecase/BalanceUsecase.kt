package com.github.alimostaghimi.currencyexchanger.domain.usecase

import com.github.alimostaghimi.currencyexchanger.domain.repository.BalanceRepository
import javax.inject.Inject

class BalanceUsecase @Inject constructor(private val balanceRepository: BalanceRepository) {
    fun getBalance() = balanceRepository.getAllCurrencies()
}