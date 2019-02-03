package com.blockchainapp.ui.model

data class TxUIModel(
    val balance: String,
    val allTransactions: List<Transaction>
)