package com.github.alimostaghimi.currencyexchanger.data.error

import com.github.alimostaghimi.currencyexchanger.domain.error.ErrorHandler
import com.github.alimostaghimi.currencyexchanger.domain.error.ServerErrorException
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseError
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class GeneralErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun getError(throwable: Throwable): BaseError {
        return when (throwable) {
            is IOException -> BaseError.Network
            is HttpException -> {
                when (throwable.code()) {
                    // not found
                    HttpURLConnection.HTTP_NOT_FOUND -> BaseError.NotFound

                    // access denied
                    HttpURLConnection.HTTP_FORBIDDEN -> BaseError.AccessDenied

                    // unavailable service
                    HttpURLConnection.HTTP_UNAVAILABLE -> BaseError.ServiceUnavailable

                    // all the others will be treated as unknown error
                    else -> BaseError.Unknown(throwable.message.toString())
                }
            }
            else -> BaseError.Unknown(throwable.message.toString())
        }
    }

    override fun getServerError(serverErrorException: ServerErrorException): BaseError =
        BaseError.ServerError(message = serverErrorException.message)
}