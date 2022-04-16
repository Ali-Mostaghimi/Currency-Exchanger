package com.github.alimostaghimi.currencyexchanger.data.datasource.local.preferences.base

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefStorage @Inject constructor(private val sharedPreferences: SharedPreferences):
    PrefStorage {

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String, defaultValue: String?): String? =
        sharedPreferences.getString(key, defaultValue)

    override fun removeString(key: String) {
        with(sharedPreferences.edit()){
            remove(key)
            apply()
        }
    }

}