package com.blockchainapp.dagger.module

import android.content.Context
import com.blockchainapp.R
import com.blockchainapp.data.converter.RawDataConverter
import com.blockchainapp.data.provider.TransactionProvider
import com.blockchainapp.data.service.TxService
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataModule(context: Context) {
    private val baseUrl: String
    private val key: String

    init {
        baseUrl = context.getString(R.string.base_url)
        key = context.getString(R.string.key)
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    fun provideTxService(
        gsonConverterFactory: GsonConverterFactory,
        rxJavaFactory: RxJava2CallAdapterFactory
    ): TxService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJavaFactory).build()
        return retrofit.create(TxService::class.java)
    }

    @Provides
    fun provideRawDataConverter(): RawDataConverter {
        return RawDataConverter()
    }

    @Provides
    fun provideTransactionProvider(
        service: TxService,
        rawDataConverter: RawDataConverter
    ): TransactionProvider {
        return TransactionProvider(service, rawDataConverter, key)
    }
}