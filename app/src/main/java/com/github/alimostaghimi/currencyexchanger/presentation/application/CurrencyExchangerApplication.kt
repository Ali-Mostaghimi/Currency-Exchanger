package com.github.alimostaghimi.currencyexchanger.presentation.application

import android.app.Application
import androidx.fragment.app.Fragment

class CurrencyExchangerApplication: Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
    }

}

fun Fragment.findApplicationComponent(): ApplicationComponent = (this.requireActivity().application as CurrencyExchangerApplication).applicationComponent