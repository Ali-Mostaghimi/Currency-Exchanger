package com.github.alimostaghimi.currencyexchanger.presentation.application

import android.content.Context
import android.content.SharedPreferences
import com.github.alimostaghimi.currencyexchanger.data.datasource.DataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.ExchangeRatesDataSynchronizer
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.DatabaseService
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base.PREF_NAME
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.transactioncount.TransactionCountPreferences
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.transactioncount.TransactionCountPreferencesImpl
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.data.error.GeneralErrorHandlerImpl
import com.github.alimostaghimi.currencyexchanger.di.ContextType
import com.github.alimostaghimi.currencyexchanger.di.NamedContext
import com.github.alimostaghimi.currencyexchanger.domain.error.ErrorHandler
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionCalculationStrategy
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.CommissionCalculator
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.DefaultCommissionCalculator
import com.github.alimostaghimi.currencyexchanger.domain.usecase.commission.FirstFiveTransactionsIsCommissionFree
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @NamedContext(ContextType.ApplicationContext)
    @Provides
    fun provideApplicationContext(application: CurrencyExchangerApplication): Context = application.applicationContext

    @Provides
    fun provideErrorHandler(errorHandler: GeneralErrorHandlerImpl): ErrorHandler = errorHandler

    @Provides
    fun provideSharedPreferences(@NamedContext(ContextType.ApplicationContext) context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDataSynchronizer(): DataSynchronizer<Any, ExchangeRatesResponse> =
        ExchangeRatesDataSynchronizer(
            dispatcher = Dispatchers.IO
        )
}