package com.github.alimostaghimi.currencyexchanger.presentation.home

import androidx.lifecycle.ViewModelProvider
import com.github.alimostaghimi.currencyexchanger.data.datasource.DataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.ExchangeRatesDataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.transactioncount.TransactionCountPreferences
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.transactioncount.TransactionCountPreferencesImpl
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesDataSource
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesDataSourceImpl
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.data.repository.BalanceRepositoryImpl
import com.github.alimostaghimi.currencyexchanger.data.repository.ExchangeRatesRepositoryImpl
import com.github.alimostaghimi.currencyexchanger.data.repository.TransactionsRepositoryImpl
import com.github.alimostaghimi.currencyexchanger.di.ExchangeRatesDataSourceType
import com.github.alimostaghimi.currencyexchanger.di.ExchangeRatesRepositoryType
import com.github.alimostaghimi.currencyexchanger.di.NamedExchangeRatesDataSource
import com.github.alimostaghimi.currencyexchanger.di.NamedExchangeRatesRepository
import com.github.alimostaghimi.currencyexchanger.domain.repository.BalanceRepository
import com.github.alimostaghimi.currencyexchanger.domain.repository.ExchangeRatesRepository
import com.github.alimostaghimi.currencyexchanger.domain.repository.TransactionsRepository
import com.github.alimostaghimi.currencyexchanger.domain.usecase.BalanceUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.GetRatesUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.GetTransactionCountUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.TransactionUsecase
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionCalculator
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.DefaultCommissionCalculator
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.FirstFiveTransactionsIsCommissionFree
import com.github.alimostaghimi.currencyexchanger.utils.viewmodel.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class HomeFragmentModule {

    @NamedExchangeRatesRepository(ExchangeRatesRepositoryType.ActualImpl)
    @Provides
    fun provideExchangeRatesRepositoryImpl(repository: ExchangeRatesRepositoryImpl): ExchangeRatesRepository =
        repository

    @NamedExchangeRatesDataSource(ExchangeRatesDataSourceType.ActualImpl)
    @Provides
    fun provideExchangeRatesDataSource(dataSource: ExchangeRatesDataSourceImpl): ExchangeRatesDataSource =
        dataSource

    @Provides
    fun provideHomeViewModel(
        fragment: HomeFragment,
        getRatesUsecase: GetRatesUsecase,
        transactionUsecase: TransactionUsecase,
        balanceUsecase: BalanceUsecase,
        getTransactionCountUsecase: GetTransactionCountUsecase
    ): HomeViewModel = ViewModelProvider(fragment,
        ViewModelProviderFactory(HomeViewModel::class) {
            HomeViewModel(
                getRatesUsecase = getRatesUsecase,
                transactionUsecase = transactionUsecase,
                balanceUsecase = balanceUsecase,
                getTransactionCountUsecase = getTransactionCountUsecase
            )
        }
    ).get(HomeViewModel::class.java)

    @Provides
    fun provideBalanceRepository(balanceRepository: BalanceRepositoryImpl): BalanceRepository =
        balanceRepository

    @Provides
    fun provideTransactionsRepository(transactionsRepository: TransactionsRepositoryImpl): TransactionsRepository =
        transactionsRepository

    @Provides
    fun provideCommissionCalculator(): CommissionCalculator =
        DefaultCommissionCalculator(
            initialCommissionCalculations = setOf(
                FirstFiveTransactionsIsCommissionFree()
            )
        )

    @Provides
    fun provideTransactionCount(transactionCountPreferences: TransactionCountPreferencesImpl): TransactionCountPreferences =
        transactionCountPreferences
}