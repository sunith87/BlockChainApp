package com.blockchainapp.dagger.module

import com.blockchainapp.ui.TxsContract
import com.blockchainapp.ui.TransactionPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class PresenterModule {

    @Binds
    abstract fun bindPresenter(presenter: TransactionPresenter): TxsContract.Presenter
}