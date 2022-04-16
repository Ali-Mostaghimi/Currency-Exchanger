package com.github.alimostaghimi.currencyexchanger.presentation.application

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesApiService
import com.github.alimostaghimi.currencyexchanger.di.modules.NetworkModule
import com.github.alimostaghimi.currencyexchanger.domain.error.ErrorHandler
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class, ApplicationModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder{
        fun build(): ApplicationComponent

        @BindsInstance
        fun application(app: CurrencyExchangerApplication): Builder
    }

    fun getErrorHandler(): ErrorHandler
    fun getExchangeRatesApiService(): ExchangeRatesApiService
}