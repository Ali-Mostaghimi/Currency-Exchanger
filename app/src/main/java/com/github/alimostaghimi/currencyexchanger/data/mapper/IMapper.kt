package com.github.alimostaghimi.currencyexchanger.data.mapper

interface IMapper<T, R> {
    fun map(data: T): R
}