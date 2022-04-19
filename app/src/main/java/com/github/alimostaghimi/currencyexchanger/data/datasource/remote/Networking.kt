package com.github.alimostaghimi.currencyexchanger.data.datasource.remote

import com.github.alimostaghimi.currencyexchanger.BuildConfig
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ExchangeRatesResponse
import com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model.ServerErrorResponse
import com.github.alimostaghimi.currencyexchanger.domain.error.ServerErrorException
import com.github.alimostaghimi.currencyexchanger.utils.flow.FlowCallAdapterFactory
import com.google.gson.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


object Networking {
    private const val NETWORK_CALL_TIMEOUT = 60


    fun createGsonConverterFactory(): GsonConverterFactory {
        val ratesDeserializer  = GsonBuilder().setLenient().registerTypeAdapter(
            ExchangeRatesResponse::class.java,
            ExchangeRatesResponseDeserializer()
        ).create()

        return GsonConverterFactory.create(ratesDeserializer)
    }

    fun buildRetrofit(
        baseUrl: String = BuildConfig.BASE_URL,
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    fun createExchangeRatesApiService(retrofit: Retrofit): ExchangeRatesApiService =
        retrofit.create(ExchangeRatesApiService::class.java)

    fun buildOkhttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE
                    })
            .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .followSslRedirects(false)
            .followRedirects(false)
            .build()
}

class ExchangeRatesResponseDeserializer : JsonDeserializer<ExchangeRatesResponse> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ExchangeRatesResponse {
        return if ((json as JsonObject)["success"].asBoolean) {
            Gson().fromJson(json, ExchangeRatesResponse::class.java)
        } else {
            val serverError = Gson().fromJson(json, ServerErrorResponse::class.java)
            throw ServerErrorException(message = serverError.error.info)
        }
    }

}