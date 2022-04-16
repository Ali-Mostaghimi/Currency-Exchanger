package com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model


import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int
): ServerBaseResponse