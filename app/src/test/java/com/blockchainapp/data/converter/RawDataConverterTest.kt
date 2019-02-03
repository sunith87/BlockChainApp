package com.blockchainapp.data.converter

import com.blockchainapp.BaseTest
import com.blockchainapp.data.model.*
import com.blockchainapp.ui.model.TransactionDetail
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import org.mockito.Mock

class RawDataConverterTest : BaseTest() {

    private lateinit var converter: RawDataConverter
    @Mock
    private lateinit var mockResponse: TxResponse
    @Mock
    private lateinit var mockWallet: Wallet
    @Mock
    private lateinit var mockInfo: Info
    @Mock
    private lateinit var mockSymbol: Symbol
    @Mock
    private lateinit var mockRawTransactionSent: RawTransaction
    @Mock
    private lateinit var mockRawTransactionReceived: RawTransaction
    @Mock
    private lateinit var mockOutputTxSent: OutputTx
    @Mock
    private lateinit var mockOutputTxReceived: OutputTx
    @Mock
    private lateinit var mockSentXpub: XPubData

    private val mockRawTransactions = mutableListOf<RawTransaction>()
    private val mockOutputsSentList = mutableListOf<OutputTx>()
    private val mockOutputsReceivedList = mutableListOf<OutputTx>()

    companion object {
        const val MOCK_FINAL_BALANCE: Long = 8549
        const val MOCK_CONVERSION: Long = 100000000
        const val MOCK_FEE: Long = 36881
        const val MOCK_SENT_RESULT: Long = -612687
        const val MOCK_RECEIVED_RESULT: Long = 559182
        const val MOCK_HASH = "mockHash"
        const val MOCK_CURRENCY_SYMBOL = "BTC"
        const val MOCK_MDATA = "someVeryLongHash"
        const val MOCK_ADDRESS = "mockAddress"
        const val MOCK_TIME: Long = 1542639840
    }

    @Before
    override fun setUp() {
        super.setUp()
        setupAllMocks()

        converter = RawDataConverter()
    }

    private fun setupAllMocks() {
        setupMockSentTransaction()
        setupMockReceivedTransaction()
        mockRawTransactions.add(mockRawTransactionSent)
        mockRawTransactions.add(mockRawTransactionReceived)

        whenever(mockResponse.info).thenReturn(mockInfo)
        whenever(mockInfo.symbol_btc).thenReturn(mockSymbol)
        whenever(mockSymbol.conversion).thenReturn(MOCK_CONVERSION)
        whenever(mockSymbol.symbol).thenReturn(MOCK_CURRENCY_SYMBOL)

        whenever(mockResponse.wallet).thenReturn(mockWallet)
        whenever(mockWallet.final_balance).thenReturn(MOCK_FINAL_BALANCE)

        whenever(mockResponse.txs).thenReturn(mockRawTransactions)
    }

    private fun setupMockReceivedTransaction() {
        whenever(mockRawTransactionReceived.fee).thenReturn(MOCK_FEE)
        whenever(mockRawTransactionReceived.result).thenReturn(MOCK_RECEIVED_RESULT)
        whenever(mockRawTransactionReceived.hash).thenReturn(MOCK_HASH)
        whenever(mockRawTransactionReceived.time).thenReturn(MOCK_TIME)
        whenever(mockOutputTxReceived.addr).thenReturn(MOCK_ADDRESS)
        whenever(mockOutputTxReceived.xpub).thenReturn(null)

        mockOutputsReceivedList.add(mockOutputTxReceived)
        whenever(mockRawTransactionSent.out).thenReturn(mockOutputsReceivedList)
    }

    private fun setupMockSentTransaction() {
        whenever(mockRawTransactionSent.fee).thenReturn(MOCK_FEE)
        whenever(mockRawTransactionSent.result).thenReturn(MOCK_SENT_RESULT)
        whenever(mockRawTransactionSent.hash).thenReturn(MOCK_HASH)
        whenever(mockRawTransactionSent.time).thenReturn(MOCK_TIME)
        whenever(mockOutputTxSent.addr).thenReturn(MOCK_ADDRESS)
        whenever(mockOutputTxSent.xpub).thenReturn(mockSentXpub)
        whenever(mockSentXpub.m).thenReturn(MOCK_MDATA)

        mockOutputsSentList.add(mockOutputTxSent)
        whenever(mockRawTransactionSent.out).thenReturn(mockOutputsSentList)
    }

    @Test
    fun testConvert() {
        val expectedBalance = "0.00008549 $MOCK_CURRENCY_SYMBOL"
        val expectedTime = "Mon, 19 Nov 2018 - 15:04 PM"
        val expectedFee = "0.00036881 $MOCK_CURRENCY_SYMBOL"
        val expectedSentResult = "-0.00612687 BTC"
        val expectedReceivedResult = "0.00559182 BTC"

        val txUIModel = converter.convert(mockResponse)
        val transactionSent = txUIModel.allTransactions.get(0)
        val transactionReceived = txUIModel.allTransactions.get(1)

        assertEquals(txUIModel.balance, expectedBalance)

        assertEquals(transactionSent.address, MOCK_ADDRESS)
        assertEquals(transactionSent.fee, expectedFee)
        assertEquals(transactionSent.hash, MOCK_HASH)
        assertEquals(transactionSent.result, expectedSentResult)
        assertEquals(transactionSent.transactionDetail, TransactionDetail.SENT)
        assertEquals(transactionSent.time, expectedTime)

        assertTrue(transactionReceived.address.isEmpty())
        assertEquals(transactionReceived.fee, expectedFee)
        assertEquals(transactionReceived.hash, MOCK_HASH)
        assertEquals(transactionReceived.result, expectedReceivedResult)
        assertEquals(transactionReceived.transactionDetail, TransactionDetail.RECEIVED)
        assertEquals(transactionReceived.time, expectedTime)
    }
}