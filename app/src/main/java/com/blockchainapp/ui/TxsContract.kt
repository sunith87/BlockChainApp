package com.blockchainapp.ui

import com.blockchainapp.data.error.Error
import com.blockchainapp.ui.model.TxUIModel

/**
 * Contract for MVP Pattern with interface for Presenter and View
 */
interface TxsContract {

    interface Presenter {
        fun getTransactions()
        fun attachView(view: View)
        fun clear()
    }

    interface View {
        fun onDataLoaded(model: TxUIModel)
        fun onError(error: Error)
        fun showFetchingStatusMessage()
    }
}