package com.blockchainapp.dagger.component

import com.blockchainapp.ui.MainActivity
import com.blockchainapp.ui.TxsContract
import com.blockchainapp.dagger.module.DataModule
import com.blockchainapp.dagger.module.PresenterModule
import dagger.Component

@Component(modules = arrayOf(PresenterModule::class, DataModule::class))
interface TxPresenterComponent {
    fun inject(mainActivity: MainActivity)
    fun txPresenter(): TxsContract.Presenter
}