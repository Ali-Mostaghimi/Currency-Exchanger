package com.github.alimostaghimi.currencyexchanger.domain.error

import com.github.alimostaghimi.currencyexchanger.domain.model.BaseError

interface ErrorHandler {

    fun getError(throwable: Throwable): BaseError
    fun getServerError(serverErrorException: ServerErrorException): BaseError
}