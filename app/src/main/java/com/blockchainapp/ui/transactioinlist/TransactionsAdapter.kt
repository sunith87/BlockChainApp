package com.blockchainapp.ui.transactioinlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blockchainapp.R
import com.blockchainapp.ui.model.Transaction

class TransactionsAdapter(private val transationList: List<Transaction>) :
    RecyclerView.Adapter<TransactionsViewHolder>() {

    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): TransactionsViewHolder {
        val itemView = LayoutInflater.from(vg.context).inflate(R.layout.transaction_inidividual_view, vg, false)
        return TransactionsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transationList.size
    }

    override fun onBindViewHolder(vh: TransactionsViewHolder, position: Int) {
        vh.bind(transationList.get(position))
    }
}