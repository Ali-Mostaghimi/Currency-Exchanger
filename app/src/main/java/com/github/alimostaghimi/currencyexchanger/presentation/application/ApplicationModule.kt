package com.github.alimostaghimi.currencyexchanger.presentation.application

import android.content.Context
import com.github.alimostaghimi.currencyexchanger.data.error.GeneralErrorHandlerImpl
import com.github.alimostaghimi.currencyexchanger.di.ContextType
import com.github.alimostaghimi.currencyexchanger.di.NamedContext
import com.github.alimostaghimi.currencyexchanger.domain.error.ErrorHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @NamedContext(ContextType.ApplicationContext)
    @Provides
    fun provideApplicationContext(application: CurrencyExchangerApplication): Context = application.applicationContext

    @Provides
    fun provideErrorHandler(errorHandler: GeneralErrorHandlerImpl): ErrorHandler = errorHandler

}