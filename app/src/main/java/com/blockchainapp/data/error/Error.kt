package com.blockchainapp.data.error

data class Error(val errorData: ErrorData, val throwable: Throwable)

enum class ErrorData(val message: String) {
    NETWORK_ERROR("Something went wrong while getting transactions. Check Network.")
}