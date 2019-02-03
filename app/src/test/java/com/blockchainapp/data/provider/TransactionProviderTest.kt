package com.blockchainapp.data.provider

import com.blockchainapp.BaseTest
import com.blockchainapp.data.converter.RawDataConverter
import com.blockchainapp.data.model.TxResponse
import com.blockchainapp.data.service.TxService
import com.blockchainapp.ui.model.TxUIModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

import org.mockito.Mock

class TransactionProviderTest : BaseTest() {

    private lateinit var provider: TransactionProvider
    @Mock
    private lateinit var mockDataConverter: RawDataConverter
    @Mock
    private lateinit var mockService: TxService
    @Mock
    private lateinit var mockTxResponse: TxResponse
    @Mock
    private lateinit var mockTxUIModel: TxUIModel

    companion object {
        const val MOCK_KEY = "mock_key"
    }

    @Before
    override fun setUp() {
        super.setUp()
        whenever(mockService.getTransactionData(eq(MOCK_KEY))).thenReturn(Observable.just(mockTxResponse))
        whenever(mockDataConverter.convert(eq(mockTxResponse))).thenReturn(mockTxUIModel)
        provider = TransactionProvider(mockService, mockDataConverter, MOCK_KEY)
    }

    @Test
    fun testRequestTransactions_shouldCallServiceGetTransaction() {
        val test = provider.requestTransactions().test()

        verify(mockService).getTransactionData(eq(MOCK_KEY))
        test.assertValue(mockTxUIModel)
    }

    @Test
    fun testRequestTransactions_shouldCallConvert_whenRequestSuccessful() {
        val test = provider.requestTransactions().test()

        verify(mockDataConverter).convert(eq(mockTxResponse))
        test.assertValue(mockTxUIModel)
    }

    @Test
    fun testRequestTransactions_shouldRetrieveFromCache_whenCalledSecondTime() {
        val callOne = provider.requestTransactions().test()
        val callTwo = provider.requestTransactions().test()

        verify(mockService, atMost(1)).getTransactionData(eq(MOCK_KEY))
        verify(mockDataConverter, atMost(1)).convert(eq(mockTxResponse))

        callOne.assertValue(mockTxUIModel)
        callTwo.assertValue(mockTxUIModel)
    }
}