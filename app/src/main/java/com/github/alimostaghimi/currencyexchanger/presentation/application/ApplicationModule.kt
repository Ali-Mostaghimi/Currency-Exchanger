package com.github.alimostaghimi.currencyexchanger.presentation.application

import android.content.Context
import android.content.SharedPreferences
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base.PREF_NAME
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

    @Provides
    fun provideSharedPreferences(@NamedContext(ContextType.ApplicationContext) context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

}