package com.vouchervault.mobile.model

data class LoyaltyCard(
    val id: String,
    val storeName: String,
    val cardNumber: String,
    val barcodeFormat: String,
    val color: Long = 0xFFEEF2FF,
    val notes: String = ""
)
