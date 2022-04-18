package com.github.alimostaghimi.currencyexchanger.domain.usecase.commission

interface CommissionCalculationStrategy {
    fun calculateCommission(request: CommissionRequest): CommissionResult
}

sealed class CommissionResult {
    class Resolved(val commission: Float) : CommissionResult()
    object NotResolved : CommissionResult()
}


class FirstFiveTransactionsIsCommissionFree : CommissionCalculationStrategy {

    override fun calculateCommission(request: CommissionRequest): CommissionResult {
        if (request is CommissionWithCountOfTransactionRequest) {

            val commission =
                if (request.transactionCountSinceNow < 5) {
                    0f
                } else {
                    0.007f
                }
            return CommissionResult.Resolved(commission = commission)
        }
        return CommissionResult.NotResolved
    }

}

class EveryLessThan200TransactionsIsCommissionFree : CommissionCalculationStrategy {

    override fun calculateCommission(request: CommissionRequest): CommissionResult {
        if (request is CommissionWithAmountOfTransactionRequest) {

            val commission =
                if (request.amountOfTransactionInEuro < 200) {
                    0f
                } else {
                    0.007f
                }
            return CommissionResult.Resolved(commission = commission)
        }
        return CommissionResult.NotResolved
    }

}

class EveryTenthTransactionsIsCommissionFree : CommissionCalculationStrategy {

    override fun calculateCommission(request: CommissionRequest): CommissionResult {
        if (request is CommissionWithCountOfTransactionRequest) {

            val commission =
                if (request.transactionCountSinceNow % 10 == 9) {
                    0f
                } else {
                    0.007f
                }
            return CommissionResult.Resolved(commission = commission)
        }
        return CommissionResult.NotResolved
    }

}