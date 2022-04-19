package com.github.alimostaghimi.currencyexchanger.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.github.alimostaghimi.currencyexchanger.R
import com.github.alimostaghimi.currencyexchanger.data.datasource.local.database.entity.Currency
import com.github.alimostaghimi.currencyexchanger.domain.model.RatesWrapper

class BuyCurrencySpinnerAdapter(private val context: Context, data: RatesWrapper?) :
    BaseAdapter() {
    private var data = data?.rates?.keys?.toList() ?: listOf()

    override fun getCount(): Int = data.size

    override fun getItem(p0: Int): Any = data[p0]

    override fun getItemId(p0: Int): Long = data[p0].hashCode().toLong()

    override fun getView(position: Int, container: View?, parent: ViewGroup?): View {
        return container ?: LayoutInflater.from(context).inflate(R.layout.currency_unit_spinner_collapsed_item, parent, false).also {
            it.findViewById<TextView>(R.id.currency_unit_name_textView).text = data[position]
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.currency_unit_spinner_expanded_item, null, false)
        if (position == data.lastIndex){
            view.findViewById<View>(R.id.separator).visibility = View.GONE
        }

        view.findViewById<TextView>(R.id.currency_unit_name_textView)
            .text = data[position]

        return view

    }

    fun setData(ratesWrapper: RatesWrapper){
        data = ratesWrapper.rates.keys.toList()
    }

    fun getDataAt(position: Int): String = data[position]
}