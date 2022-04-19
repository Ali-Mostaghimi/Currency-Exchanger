package com.github.alimostaghimi.currencyexchanger.presentation.customview

import android.view.View
import android.view.ViewGroup

class StateViewController(
    parent: ViewGroup,
    private val layoutParams: ViewGroup.LayoutParams
) {
    private var parent: ViewGroup? = parent
    private val stateLayoutList: HashMap<String, View> = hashMapOf()
    var currentStateView: View? = null
        private set


    fun setView(key: String, view: View) {
        stateLayoutList[key] = view
    }

    fun getView(key: String) =
        stateLayoutList[key]

    fun showView(key: String){
        parent?.removeView(currentStateView)
        currentStateView = stateLayoutList[key]
        parent?.addView(currentStateView, layoutParams)
    }

    fun showView(view: View){
        parent?.removeView(currentStateView)
        currentStateView = view
        parent?.addView(currentStateView, layoutParams)
    }

    fun releaseAllViews() {
        stateLayoutList.clear()
        currentStateView = null
        parent = null
    }
}