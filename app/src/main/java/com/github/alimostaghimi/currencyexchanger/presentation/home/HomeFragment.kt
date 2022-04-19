package com.github.alimostaghimi.currencyexchanger.presentation.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.alimostaghimi.currencyexchanger.R
import com.github.alimostaghimi.currencyexchanger.domain.model.BaseResponse
import com.github.alimostaghimi.currencyexchanger.domain.model.TransactionErrorState
import com.github.alimostaghimi.currencyexchanger.domain.model.TransactionResult
import com.github.alimostaghimi.currencyexchanger.presentation.application.findApplicationComponent
import com.github.alimostaghimi.currencyexchanger.presentation.base.BaseFragment
import com.github.alimostaghimi.currencyexchanger.presentation.customview.StateViewController
import com.github.alimostaghimi.currencyexchanger.presentation.home.balancerecyclerview.MyBalanceRecyclerViewAdapter
import com.github.alimostaghimi.currencyexchanger.utils.extensions.toErrorMessage
import com.github.alimostaghimi.currencyexchanger.utils.extensions.toStringWithFloatingPoint
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun provideLayoutId(): Int = R.layout.fragment_home

    private lateinit var stateViewController: StateViewController

    private var fragmentView: View? = null
    private var sellAmountEditText: EditText? = null
    private var buyAmountTextView: TextView? = null

    private lateinit var buyCurrencySpinnerAdapter: BuyCurrencySpinnerAdapter
    private lateinit var sellCurrencySpinnerAdapter: SellCurrencySpinnerAdapter
    private lateinit var myBalanceAdapter: MyBalanceRecyclerViewAdapter


    private var sellCurrencyUnit: String? = null
    private var buyCurrencyUnit: String? = null

    override fun injectDependencies() {
        DaggerHomeFragmentComponent.builder()
            .applicationComponent(findApplicationComponent())
            .fragment(this)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentView = view
        val parent = view.findViewById<ConstraintLayout>(R.id.container_constraintLayout)

        setupStateViewController(parent)

        setupBuyAndSellSpinners()

        setupSellCurrencyEditText()

        setupBuyAmountTextView()

        setupMyBalanceRecyclerView()

        setupSubmitButton()

        updateExchangeRates()
    }

    private fun setupSubmitButton() {
        fragmentView?.findViewById<MaterialButton>(R.id.exchange_currency_submit_button)
            ?.apply {
                setOnClickListener {
                    viewModel.exchange(
                        sourceUnit = sellCurrencyUnit, destinationUnit = buyCurrencyUnit,
                        transactionAmount = sellAmountEditText?.text?.toString()?.toDoubleOrNull()
                    )
                }
            }
    }

    private fun setupBuyAmountTextView() {
        buyAmountTextView =
            fragmentView?.findViewById(R.id.exchange_currency_buy_component_buy_amount_textView)
    }

    private fun setupMyBalanceRecyclerView() {
        fragmentView?.findViewById<RecyclerView>(R.id.my_balance_recyclerView)
            ?.apply {
                myBalanceAdapter = MyBalanceRecyclerViewAdapter()
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = myBalanceAdapter
            }
    }

    private fun setupSellCurrencyEditText() {
        sellAmountEditText =
            fragmentView?.findViewById<EditText>(R.id.exchange_currency_sell_component_sell_amount_editText)
                .also {
                    it?.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            updateBuyTextView()
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })
                }
    }

    private fun setupBuyAndSellSpinners() {
        fragmentView?.findViewById<Spinner>(R.id.exchange_currency_buy_component_sell_currency_unit_spinner)
            ?.apply {
                buyCurrencySpinnerAdapter = BuyCurrencySpinnerAdapter(requireContext(), null)
                adapter = buyCurrencySpinnerAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        pos: Int,
                        p3: Long
                    ) {
                        buyCurrencyUnit = (p0?.adapter as BuyCurrencySpinnerAdapter).getDataAt(pos)
                        updateBuyTextView()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        updateBuyTextView()
                    }

                }
            }

        fragmentView?.findViewById<Spinner>(R.id.exchange_currency_sell_component_sell_currency_unit_spinner)
            ?.apply {
                sellCurrencySpinnerAdapter = SellCurrencySpinnerAdapter(requireContext(), listOf())
                adapter = sellCurrencySpinnerAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        pos: Int,
                        p3: Long
                    ) {
                        sellCurrencyUnit =
                            (p0?.adapter as SellCurrencySpinnerAdapter).getDataAt(pos).currencyUnit
                        updateBuyTextView()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        updateBuyTextView()
                    }

                }
            }
    }

    private fun setupStateViewController(parent: ConstraintLayout) {
        stateViewController =
            StateViewController(parent, ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToBottom = R.id.home_fragment_appBar_layout
                bottomToTop = R.id.my_balance_recyclerView
                verticalBias = 0f
            })
        stateViewController.setView(
            NetworkViewState.Loading.name,
            LayoutInflater.from(context).inflate(R.layout.loading_layout, parent, false)
        )
        stateViewController.setView(
            NetworkViewState.Success.name,
            LayoutInflater.from(context).inflate(R.layout.success_layout, parent, false)
        )
        stateViewController.setView(
            NetworkViewState.Error.name,
            LayoutInflater.from(context).inflate(R.layout.error_layout, parent, false)
        )
    }

    fun updateBuyTextView() {
        val buyAmount = viewModel.calculateTransactionForPreview(
            sellCurrencyUnit,
            buyCurrencyUnit,
            sellAmountEditText?.text?.toString()?.toDoubleOrNull()
        )
        buyAmountTextView?.text = buyAmount?.let { "+$it" } ?: ""
    }

    private fun updateExchangeRates() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.rates
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .onStart {
                        emit(BaseResponse.Loading)
                    }
                    .collect { baseResponse ->
                        when (baseResponse) {
                            is BaseResponse.Loading -> {
                                stateViewController.showView(NetworkViewState.Loading.name)
                            }
                            is BaseResponse.Error -> {
                                stateViewController.getView(NetworkViewState.Error.name)
                                    .also { errorLayout ->
                                        //prepare error layout
                                        prepareErrorLayout(errorLayout, baseResponse)
                                    }?.also {
                                        stateViewController.showView(it)
                                    }
                            }
                            is BaseResponse.Success -> {
                                stateViewController.showView(NetworkViewState.Success.name)
                                buyCurrencySpinnerAdapter.setData(baseResponse.data)
                                buyCurrencySpinnerAdapter.notifyDataSetChanged()
                                updateBuyTextView()
                            }
                        }
                    }
            }

            launch {
                viewModel.balance
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        myBalanceAdapter.setData(it)
                        //update sell spinner to show only currencies available in wallet
                        sellCurrencySpinnerAdapter.setData(it)
                        sellCurrencySpinnerAdapter.notifyDataSetChanged()
                    }
            }

            launch {
                viewModel.transactionResult
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect { transactionResultEvent ->
                        when (val transactionResult = transactionResultEvent.getIfNotHandled()) {
                            is TransactionResult.Error -> {
                                val stringResource =
                                    when (transactionResult.transactionErrorState) {
                                        TransactionErrorState.RatesAreNotUpToDated -> R.string.transaction_result_error_rates_are_not_up_to_date
                                        TransactionErrorState.BalanceIsNotEnough -> R.string.transaction_result_error_balance_is_not_enough
                                        TransactionErrorState.SourceUnitIsNotSelected -> R.string.transaction_result_error_source_unit_is_not_selected
                                        TransactionErrorState.DestinationUnitIsNotSelected -> R.string.transaction_result_error_destination_unit_is_not_selected
                                        TransactionErrorState.AmountOfTransactionIsNotSpecified -> R.string.transaction_result_error_amount_of_transaction_is_not_specified
                                        TransactionErrorState.CanNotConvertSameCurrencies -> R.string.transaction_result_error_can_not_convert_the_same_currencies
                                    }
                                fragmentView?.let {
                                    Snackbar.make(
                                        it,
                                        getString(stringResource),
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            is TransactionResult.Success -> {
                                showDialog(
                                    "You have converted ${transactionResult.sourceAmount} ${transactionResult.sourceUnit} to ${transactionResult.destinationAmount.toStringWithFloatingPoint()} ${transactionResult.destinationUnit}. Commission Fee - ${
                                        transactionResult.commissionFee.toStringWithFloatingPoint(
                                            4
                                        )
                                    } ${transactionResult.sourceUnit}"
                                )
                            }
                        }
                    }
            }

        }
    }

    private fun prepareErrorLayout(errorLayout: View?, baseResponse: BaseResponse.Error) {
        errorLayout?.findViewById<TextView>(R.id.message_textView)
            ?.text = baseResponse.error.toErrorMessage(requireContext())
        errorLayout?.findViewById<TextView>(R.id.retry_textView)
            ?.setOnClickListener {
                viewModel.getRates()
            }
    }

    private fun showDialog(message: String) {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.transaction_result_dialog)
        dialog.setCanceledOnTouchOutside(true)
        dialog.findViewById<TextView>(R.id.transaction_result_dialog_message_textView)
            .text = message
        dialog.findViewById<TextView>(R.id.transaction_result_dialog_done_textView)
            .setOnClickListener {
                dialog.dismiss()
            }
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getRates()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopGettingRates()
    }

    override fun onDestroyView() {
        stateViewController.releaseAllViews()
        fragmentView = null
        sellAmountEditText = null
        super.onDestroyView()
    }

    enum class NetworkViewState {
        Loading, Success, Error
    }
}