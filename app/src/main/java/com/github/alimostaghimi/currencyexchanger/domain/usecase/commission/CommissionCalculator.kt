package com.github.alimostaghimi.currencyexchanger.domain.usecase.commission

import javax.inject.Inject

/**
 * calculates commissions with the help of strategies
 * and treats to lowest commission between calculated commissions as the best commission
 */
class DefaultCommissionCalculator(initialCommissionCalculations: Set<CommissionCalculationStrategy>): CommissionCalculator {

    private val commissionCalculationStrategies: MutableSet<CommissionCalculationStrategy> =
        initialCommissionCalculations.toMutableSet()

    override fun calculateCommission(commissionRequest: CommissionRequest): Float =
        commissionCalculationStrategies.map {
            it.calculateCommission(commissionRequest)
        }.filterIsInstance<CommissionResult.Resolved>()
            .sortedWith(compareBy { it.commission })
            .first()
            .commission

    override fun registerCommissionCalculatorStrategy(commissionCalculationStrategy: CommissionCalculationStrategy) {
        commissionCalculationStrategies.add(commissionCalculationStrategy)
    }
}

interface CommissionCalculator{
    fun calculateCommission(commissionRequest: CommissionRequest): Float
    fun registerCommissionCalculatorStrategy(commissionCalculationStrategy: CommissionCalculationStrategy)
}