package com.github.alimostaghimi.currencyexchanger.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.alimostaghimi.currencyexchanger.R
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.presentation.application.findApplicationComponent
import com.github.alimostaghimi.currencyexchanger.presentation.base.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun provideLayoutId(): Int = R.layout.fragment_home

    override fun injectDependencies() {
        DaggerHomeFragmentComponent.builder()
            .applicationComponent(findApplicationComponent())
            .fragment(this)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateExchangeRates()
    }

    private fun updateExchangeRates() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.rates
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .onStart {
                        emit(BaseResponse.Loading)
                    }
                    .collect {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }

            launch {
                viewModel.balance
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }

        }
    }
}