package com.blockchainapp.ui

import com.blockchainapp.BaseTest
import com.blockchainapp.data.error.Error
import com.blockchainapp.data.error.ErrorData
import com.blockchainapp.data.provider.TransactionProvider
import com.blockchainapp.ui.model.TxUIModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TransactionPresenterTest : BaseTest() {

    private lateinit var presenter: TransactionPresenter

    @Mock
    private lateinit var mockDataProvider: TransactionProvider
    @Mock
    private lateinit var mockView: TxsContract.View
    @Mock
    private lateinit var mockTXUIModel: TxUIModel
    @Mock
    private lateinit var mockThrowable: Throwable

    @Before
    override fun setUp() {
        super.setUp()
        presenter = TransactionPresenter(mockDataProvider)
        presenter.attachView(mockView)
    }

    @Test
    fun testGetTransactions_shouldCallShowFetchingStatusMessage() {
        whenever(mockDataProvider.requestTransactions()).thenReturn(Observable.just(mockTXUIModel))
        presenter.getTransactions()

        verify(mockView).showFetchingStatusMessage()
    }

    @Test
    fun testGetTransactions_shouldCallOnDataLoaded_whenDataProviderCallSuccessful() {
        whenever(mockDataProvider.requestTransactions()).thenReturn(Observable.just(mockTXUIModel))
        presenter.getTransactions()

        verify(mockView).showFetchingStatusMessage()
        verify(mockView).onDataLoaded(eq(mockTXUIModel))
        verify(mockView, never()).onError(any())
    }

    @Test
    fun testGetTransactions_shouldCallOnError_whenDataProviderCallErros() {
        whenever(mockDataProvider.requestTransactions()).thenReturn(Observable.error(mockThrowable))
        presenter.getTransactions()

        val errorCapture = argumentCaptor<Error>()
        verify(mockView).onError(errorCapture.capture())
        assertEquals(ErrorData.NETWORK_ERROR, errorCapture.firstValue.errorData)
    }
}