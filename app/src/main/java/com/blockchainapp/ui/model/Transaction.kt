package com.blockchainapp.ui.model

data class Transaction(
    val result: String,
    val transactionDetail: TransactionDetail,
    val fee: String,
    val hash: String,
    val address: String = "",
    val time: String
)