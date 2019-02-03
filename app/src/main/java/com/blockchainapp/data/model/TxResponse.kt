package com.blockchainapp.data.model

data class TxResponse(
    val info: Info,
    val wallet: Wallet,
    val txs: List<RawTransaction>
)