package com.github.alimostaghimi.currencyexchanger.presentation.home.balancerecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.alimostaghimi.currencyexchanger.R
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.utils.extensions.toStringWithFloatingPoint
import java.lang.IllegalStateException


private const val MY_BALANCE_TITLE_VIEW_TYPE = 1
private const val CURRENCY_VIEW_TYPE = 2

class MyBalanceRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<MyBalanceRecyclerRowModel>() {
        override fun areItemsTheSame(
            oldItem: MyBalanceRecyclerRowModel,
            newItem: MyBalanceRecyclerRowModel
        ): Boolean =
            if (oldItem is MyBalanceRecyclerRowModel.MyBalanceTitleRowModel &&
                newItem is MyBalanceRecyclerRowModel.MyBalanceTitleRowModel
            ) {
                oldItem == newItem
            } else if (oldItem is MyBalanceRecyclerRowModel.CurrencyRowModel &&
                newItem is MyBalanceRecyclerRowModel.CurrencyRowModel
            ) {
                oldItem == newItem
            } else {
                false
            }

        override fun areContentsTheSame(
            oldItem: MyBalanceRecyclerRowModel,
            newItem: MyBalanceRecyclerRowModel
        ): Boolean =
            if (oldItem is MyBalanceRecyclerRowModel.MyBalanceTitleRowModel &&
                newItem is MyBalanceRecyclerRowModel.MyBalanceTitleRowModel
            ) {
                oldItem.text == newItem.text
            } else if (oldItem is MyBalanceRecyclerRowModel.CurrencyRowModel &&
                newItem is MyBalanceRecyclerRowModel.CurrencyRowModel
            ) {
                oldItem.currencyUnit == newItem.currencyUnit
            } else {
                false
            }

    }
    private val data: AsyncListDiffer<MyBalanceRecyclerRowModel> =
        AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val viewHolder = when (viewType) {
            MY_BALANCE_TITLE_VIEW_TYPE -> {
                TotalBalanceTitleViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_item_total_balance_text, parent, false)
                )
            }
            CURRENCY_VIEW_TYPE -> {
                CurrencyViewViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_item_my_balance, parent, false)
                )
            }
            else -> {
                throw IllegalStateException("Not Implemented for viewType = $viewType")
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.setData(data.currentList[position])
    }

    override fun getItemCount(): Int = data.currentList.size

    fun setData(newData: List<Currency>) {
        val list: MutableList<MyBalanceRecyclerRowModel> = mutableListOf()
        list.add(MyBalanceRecyclerRowModel.MyBalanceTitleRowModel("Total balance"))
        list.addAll(newData.map {
            MyBalanceRecyclerRowModel.CurrencyRowModel(
                currencyUnit = it.currencyUnit,
                amount = it.amount
            )
        })
        data.submitList(list)
    }

    override fun getItemViewType(position: Int): Int =
        when (data.currentList[position]) {
            is MyBalanceRecyclerRowModel.MyBalanceTitleRowModel ->
                MY_BALANCE_TITLE_VIEW_TYPE
            is MyBalanceRecyclerRowModel.CurrencyRowModel ->
                CURRENCY_VIEW_TYPE
        }

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        holder.cleanUp()
    }
}

sealed interface MyBalanceRecyclerRowModel {
    data class CurrencyRowModel(
        override val currencyUnit: String,
        override val amount: Double
    ) : Currency(currencyUnit, amount), MyBalanceRecyclerRowModel

    class MyBalanceTitleRowModel(val text: String) : MyBalanceRecyclerRowModel
}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun setData(data: MyBalanceRecyclerRowModel)
    abstract fun cleanUp()
}

class TotalBalanceTitleViewHolder(itemView: View) : BaseViewHolder(itemView) {
    private var totalBalanceTitle: TextView =
        itemView.findViewById(R.id.my_balance_total_balance_title_textView)

    override fun setData(data: MyBalanceRecyclerRowModel) {
        if (data is MyBalanceRecyclerRowModel.MyBalanceTitleRowModel) {
            totalBalanceTitle.text = data.text
        }
    }

    override fun cleanUp() {
        totalBalanceTitle.text = ""
    }
}

class CurrencyViewViewHolder(itemView: View) : BaseViewHolder(itemView) {
    private var amountTextView: TextView =
        itemView.findViewById(R.id.my_balance_currency_amount_textView)
    private var unitTextView: TextView =
        itemView.findViewById(R.id.my_balance_currency_unit_textView)

    override fun setData(data: MyBalanceRecyclerRowModel) {
        if (data is MyBalanceRecyclerRowModel.CurrencyRowModel) {
            amountTextView.text = data.amount.toStringWithFloatingPoint()
            unitTextView.text = data.currencyUnit
        }
    }

    override fun cleanUp() {
        amountTextView.text = ""
        unitTextView.text = ""
    }
}

