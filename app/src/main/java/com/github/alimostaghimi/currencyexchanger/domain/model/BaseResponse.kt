package com.github.alimostaghimi.currencyexchanger.domain.model

sealed class BaseResponse<out T> {

    data class Success<out T>(val data: T) : BaseResponse<T>()

    data class Error(val error: BaseError) : BaseResponse<Nothing>()

    object Loading : BaseResponse<Nothing>()

}