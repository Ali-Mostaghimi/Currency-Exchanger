package com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base

interface PrefStorage {
    fun setString(key: String, value: String)
    fun getString(key: String, defaultValue: String? = null): String?
    fun removeString(key: String)
}