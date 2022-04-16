package com.github.alimostaghimi.currencyexchanger.domain.error

data class ServerErrorException(override val message: String? = "", override val cause: Throwable? = null): Exception()