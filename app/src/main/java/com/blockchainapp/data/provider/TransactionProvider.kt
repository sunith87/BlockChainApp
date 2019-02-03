package com.blockchainapp.data.provider

import com.blockchainapp.data.converter.RawDataConverter
import com.blockchainapp.ui.model.TxUIModel
import com.blockchainapp.data.service.TxService
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

/**
 * This class provides data to the Caller.
 * Data is provided either from cache if present or a call is made using the service to retrieve data
 */
class TransactionProvider(
    private val service: TxService,
    private val converter: RawDataConverter,
    private val key: String
) {

    private val subject: BehaviorSubject<TxUIModel> = BehaviorSubject.create()

    fun requestTransactions(): Observable<TxUIModel> {
        if (subject.hasValue()) {
            return subject
        } else {
            return service.getTransactionData(key)
                .map {
                    converter.convert(it)
                }.doOnNext(Consumer { data -> cacheModel(data) })
        }
    }

    private fun cacheModel(data: TxUIModel) {
        subject.onNext(data)
    }
}