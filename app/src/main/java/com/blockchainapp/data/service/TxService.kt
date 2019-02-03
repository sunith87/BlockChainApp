package com.blockchainapp.data.service

import com.blockchainapp.data.model.TxResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TxService {
    @GET("/multiaddr")
    fun getTransactionData(@Query("active") key: String): Observable<TxResponse>
}