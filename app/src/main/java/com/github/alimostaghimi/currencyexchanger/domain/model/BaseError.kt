package com.github.alimostaghimi.currencyexchanger.domain.model

sealed class BaseError {

    object Network : BaseError()
    object NotFound : BaseError()
    object AccessDenied : BaseError()
    object ServiceUnavailable : BaseError()

    data class ServerError(val message: String?): BaseError()

    data class Unknown(val message: String?) : BaseError()

}