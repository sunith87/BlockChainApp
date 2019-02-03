package com.blockchainapp.data.converter

import com.blockchainapp.data.model.Info
import com.blockchainapp.data.model.OutputTx
import com.blockchainapp.data.model.RawTransaction
import com.blockchainapp.data.model.TxResponse
import com.blockchainapp.ui.model.Transaction
import com.blockchainapp.ui.model.TransactionDetail
import com.blockchainapp.ui.model.TxUIModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * - Encapsultes the logic to convert raw data into data useful for UI.
 * - This layer can clear out any null or empty values and only provide data if present.
 * - This will lead to less crashes
 * - Since all this logic is done here, UI has to just render data provided from this class
 * instead of continously evaluating this logic when user scrolls the list up and down
 */
class RawDataConverter {

    companion object {
        const val DATE_FORMAT = "EEE, d MMM yyyy - HH:mm aaa"
        const val BTC_FORMAT = "%.8f"
        const val MILLISECONDS = 1000
    }

    fun convert(txResponse: TxResponse): TxUIModel {
        return TxUIModel(
            getBalanceBTC(txResponse),
            getTransaction(txResponse.txs, txResponse.info)
        )
    }

    private fun getBalanceBTC(txResponse: TxResponse): String {
        val final_balance = txResponse.wallet.final_balance
        val btcVal = final_balance.toFloat() / txResponse.info.symbol_btc.conversion.toFloat()
        val symbol = txResponse.info.symbol_btc.symbol
        val formattedBtcVal = BTC_FORMAT.format(btcVal)
        return "$formattedBtcVal $symbol"
    }

    private fun getTransaction(
        txs: List<RawTransaction>,
        info: Info
    ): List<Transaction> {
        return txs.map {
            convertToTransaction(it, info)
        }
    }

    private fun convertToTransaction(
        transaction: RawTransaction,
        info: Info
    ): Transaction {
        return Transaction(
            getResultBTC(transaction, info),
            getTransactionResult(transaction.result),
            getFeeBTC(transaction, info),
            transaction.hash,
            getAddress(transaction.out),
            getFormattedTime(transaction.time)
        )
    }

    private fun getFeeBTC(transaction: RawTransaction, info: Info): String {
        val fee = transaction.fee
        val btcVal = fee.toFloat() / info.symbol_btc.conversion.toFloat()
        val symbol = info.symbol_btc.symbol
        val formattedBtcVal = BTC_FORMAT.format(btcVal)
        return "$formattedBtcVal $symbol"
    }

    private fun getResultBTC(transaction: RawTransaction, info: Info): String {
        val result = transaction.result
        val btcVal = result.toFloat() / info.symbol_btc.conversion.toFloat()
        val symbol = info.symbol_btc.symbol
        val formattedBtcVal = BTC_FORMAT.format(btcVal)
        return "$formattedBtcVal $symbol"
    }

    private fun getTransactionResult(result: Long): TransactionDetail {
        return if (result > 0) {
            TransactionDetail.RECEIVED
        } else {
            TransactionDetail.SENT
        }
    }

    private fun getFormattedTime(time: Long): String {
        val date = Date(time * MILLISECONDS)
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun getAddress(outputTxs: List<OutputTx>): String {
        for (outputTx in outputTxs) {
            if (outputTx.xpub?.m.isNullOrEmpty()) {
                return outputTx.addr
            }
        }
        return ""
    }
}