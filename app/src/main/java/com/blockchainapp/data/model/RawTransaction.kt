package com.blockchainapp.data.model

data class RawTransaction(
    val result: Long,
    val fee: Long,
    val hash: String,
    val out: List<OutputTx>,
    val time: Long
)