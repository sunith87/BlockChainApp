package com.blockchainapp.ui

import com.blockchainapp.data.error.Error
import com.blockchainapp.data.error.ErrorData
import com.blockchainapp.ui.model.TxUIModel
import com.blockchainapp.data.provider.TransactionProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import javax.inject.Inject

class TransactionPresenter @Inject constructor(
    private val dataProvider: TransactionProvider
) : TxsContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: WeakReference<TxsContract.View>

    override fun attachView(view: TxsContract.View) {
        this.view = WeakReference(view)
    }

    override fun getTransactions() {
        view.get()?.showFetchingStatusMessage()
        dataProvider.requestTransactions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                handleSuccessfulResponse(it)
            }, Consumer {
                handlerError(it)
            })
    }

    private fun handlerError(it: Throwable) {
        view.get()?.onError(Error(ErrorData.NETWORK_ERROR, it))
    }

    private fun handleSuccessfulResponse(it: TxUIModel) {
        view.get()?.onDataLoaded(it)
    }

    override fun clear() {
        compositeDisposable.clear()
        view.clear()
    }
}