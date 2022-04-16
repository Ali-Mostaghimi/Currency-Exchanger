package com.github.alimostaghimi.currencyexchanger.data.datasource.remote.model


import com.google.gson.annotations.SerializedName

data class ServerErrorResponse(
    @SerializedName("error")
    val error: Error,
    @SerializedName("success")
    val success: Boolean
)