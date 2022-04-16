package com.github.alimostaghimi.currencyexchanger.utils.extensions

import android.content.Context
import com.github.alimostaghimi.currencyexchanger.R
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseError

fun BaseError.toErrorMessage(context: Context): String =
    when (this) {
        is BaseError.AccessDenied -> context.getString(R.string.accessDenied)
        is BaseError.Network -> context.getString(R.string.networkError)
        is BaseError.NotFound -> context.getString(R.string.notFound)
        is BaseError.ServiceUnavailable -> context.getString(R.string.serviceUnavailable)
        is BaseError.ServerError -> message ?: context.getString(R.string.unknownError)
        is BaseError.Unknown -> message ?: context.getString(R.string.unknownError)
    }
