package com.github.alimostaghimi.currencyexchanger.presentation.application

import com.github.alimostaghimi.currencyexchanger.data.datasource.DataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.DatabaseService
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base.PrefStorage
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesApiService
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.di.modules.NetworkModule
import com.github.alimostaghimi.currencyexchanger.di.modules.StorageModule
import com.github.alimostaghimi.currencyexchanger.domain.error.ErrorHandler
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionCalculationStrategy
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        NetworkModule::class,
        StorageModule::class,
        ApplicationModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun application(app: CurrencyExchangerApplication): Builder
    }

    fun getErrorHandler(): ErrorHandler
    fun getExchangeRatesApiService(): ExchangeRatesApiService
    fun getDataSynchronizer(): DataSynchronizer<Any, ExchangeRatesResponse>
    fun getDatabaseService(): DatabaseService
    fun getPrefStorage(): PrefStorage
}