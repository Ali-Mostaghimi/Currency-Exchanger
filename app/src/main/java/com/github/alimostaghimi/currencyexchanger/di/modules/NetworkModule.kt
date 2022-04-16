package com.github.alimostaghimi.currencyexchanger.di.modules

import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.ExchangeRatesApiService
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.Networking
import com.github.alimostaghimi.currencyexchanger.di.ConverterFactoryType
import com.github.alimostaghimi.currencyexchanger.di.NamedConverterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideExchangeRatesApiService(retrofit: Retrofit): ExchangeRatesApiService =
        Networking.createExchangeRatesApiService(retrofit)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @NamedConverterFactory(name = ConverterFactoryType.GsonConverterFactory) converterFactory: Converter.Factory
    ): Retrofit = Networking.buildRetrofit(converterFactory = converterFactory, okHttpClient = okHttpClient)

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient = Networking.buildOkhttpClient()

    @Provides
    @NamedConverterFactory(name = ConverterFactoryType.GsonConverterFactory)
    fun provideConverterFactory(): Converter.Factory = Networking.createGsonConverterFactory()

}