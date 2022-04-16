package com.github.alimostaghimi.currencyexchanger.presentation.main

import android.os.Bundle
import com.github.alimostaghimi.currencyexchanger.R
import com.github.alimostaghimi.currencyexchanger.presentation.base.BaseActivity
import com.github.alimostaghimi.currencyexchanger.presentation.home.HomeFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }

    override fun provideLayoutId(): Int = R.layout.activity_main

    override fun injectDependencies() {

    }
}