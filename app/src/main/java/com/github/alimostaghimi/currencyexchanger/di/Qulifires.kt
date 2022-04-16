package com.github.alimostaghimi.currencyexchanger.di

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesDataSource
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class NamedContext(val name: ContextType)
enum class ContextType{
    ApplicationContext, Context
}

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class NamedConverterFactory(val name: ConverterFactoryType)
enum class ConverterFactoryType{
    GsonConverterFactory
}

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class NamedExchangeRatesRepository(val name: ExchangeRatesRepositoryType)
enum class ExchangeRatesRepositoryType{
    ActualImpl, FakeImpl
}

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class NamedExchangeRatesDataSource(val name: ExchangeRatesDataSourceType)
enum class ExchangeRatesDataSourceType{
    ActualImpl, FakeImpl
}