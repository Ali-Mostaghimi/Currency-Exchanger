package com.github.alimostaghimi.currencyexchanger.domain.usecase

import com.github.alimostaghimi.currencyexchanger.domain.repository.TransactionsRepository
import javax.inject.Inject

class GetTransactionCountUsecase @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) {
    fun getTransactionCount(): Int = transactionsRepository.getTransactionCount()
}