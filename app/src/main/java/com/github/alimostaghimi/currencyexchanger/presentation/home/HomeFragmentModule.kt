package com.github.alimostaghimi.currencyexchanger.presentation.home

import androidx.lifecycle.ViewModelProvider
import com.github.alimostaghimi.currencyexchanger.data.datasource.DataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.ExchangeRatesDataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesDataSource
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesDataSourceImpl
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.data.repository.ExchangeRatesRepositoryImpl
import com.github.alimostaghimi.currencyexchanger.di.ExchangeRatesDataSourceType
import com.github.alimostaghimi.currencyexchanger.di.ExchangeRatesRepositoryType
import com.github.alimostaghimi.currencyexchanger.di.NamedExchangeRatesDataSource
import com.github.alimostaghimi.currencyexchanger.di.NamedExchangeRatesRepository
import com.github.alimostaghimi.currencyexchanger.domain.repository.ExchangeRatesRepository
import com.github.alimostaghimi.currencyexchanger.domain.usecase.GetRatesUsecase
import com.github.alimostaghimi.currencyexchanger.utils.viewmodel.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

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
    fun provideDataSynchronizer(): DataSynchronizer<Any, ExchangeRatesResponse> =
        ExchangeRatesDataSynchronizer(
            period = 5 * 1000L, /* 5 second */
            dispatcher = Dispatchers.IO
        )

    @Provides
    fun provideHomeViewModel(
        fragment: HomeFragment,
        getRatesUsecase: GetRatesUsecase
    ): HomeViewModel = ViewModelProvider(fragment,
        ViewModelProviderFactory(HomeViewModel::class) {
            HomeViewModel(getRatesUsecase = getRatesUsecase)
        }
    ).get(HomeViewModel::class.java)
}