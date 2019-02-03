package com.blockchainapp.ui.transactioinlist

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.blockchainapp.R
import com.blockchainapp.ui.model.Transaction
import com.blockchainapp.ui.model.TransactionDetail

class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvDate: TextView
    private val tvTransactionResult: TextView
    private val tvResult: TextView
    private val tvHash: TextView
    private val tvFee: TextView
    private val tvAddress: TextView
    private val tvAddressLabel: TextView

    init {
        tvDate = itemView.findViewById(R.id.tvDate)
        tvTransactionResult = itemView.findViewById(R.id.tvTransactionResult)
        tvResult = itemView.findViewById(R.id.tvResult)
        tvHash = itemView.findViewById(R.id.tvHash)
        tvFee = itemView.findViewById(R.id.tvFee)
        tvAddress = itemView.findViewById(R.id.tvAddress)
        tvAddressLabel = itemView.findViewById(R.id.tvAddressLabel)
    }

    fun bind(transaction: Transaction) {
        setDate(transaction)
        setTransactionDetailAndResult(transaction)
        setHash(transaction)
        setFee(transaction)
        setAddress(transaction)
    }

    private fun setAddress(transaction: Transaction) {
        if (transaction.address.isEmpty()) {
            tvAddress.visibility = View.GONE
            tvAddressLabel.visibility = View.GONE
        } else {
            tvAddress.visibility = View.VISIBLE
            tvAddressLabel.visibility = View.VISIBLE
            tvAddress.text = transaction.address
        }
    }

    private fun setFee(transaction: Transaction) {
        tvFee.text = transaction.fee
    }

    private fun setHash(transaction: Transaction) {
        tvHash.text = transaction.hash
    }

    private fun setResult(transaction: Transaction) {
        tvResult.text = transaction.result
    }

    private fun setDate(transaction: Transaction) {
        tvDate.text = transaction.time
    }

    private fun setTransactionDetailAndResult(transaction: Transaction) {
        val transactionResult = transaction.transactionDetail
        when (transactionResult) {
            TransactionDetail.SENT -> {
                val sentColor = ContextCompat.getColor(itemView.context, R.color.sentColor)
                tvTransactionResult.setTextColor(sentColor)
                tvResult.setTextColor(sentColor)
            }
            TransactionDetail.RECEIVED -> {
                val receivedColor = ContextCompat.getColor(itemView.context, R.color.receivedColor)
                tvTransactionResult.setTextColor(receivedColor)
                tvResult.setTextColor(receivedColor)
            }
        }
        tvTransactionResult.text = transactionResult.name
        setResult(transaction)
    }
}