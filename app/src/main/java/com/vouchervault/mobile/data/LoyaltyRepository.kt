package com.vouchervault.mobile.data

import androidx.compose.runtime.mutableStateListOf
import com.vouchervault.mobile.model.LoyaltyCard
import java.util.UUID

class LoyaltyRepository {
    private val _cards = mutableStateListOf(
        LoyaltyCard(
            id = UUID.randomUUID().toString(),
            storeName = "Klarna Shop",
            cardNumber = "9900112233",
            barcodeFormat = "CODE_128",
            color = 0xFFEFF3FF
        ),
        LoyaltyCard(
            id = UUID.randomUUID().toString(),
            storeName = "BioMarket",
            cardNumber = "123456789012",
            barcodeFormat = "EAN_13",
            color = 0xFFFFF4E6
        ),
        LoyaltyCard(
            id = UUID.randomUUID().toString(),
            storeName = "Cinema Club",
            cardNumber = "847362514",
            barcodeFormat = "CODE_39",
            color = 0xFFE6F7F1
        )
    )

    val cards: List<LoyaltyCard> = _cards

    fun addCard(storeName: String, number: String, format: String) {
        _cards.add(
            LoyaltyCard(
                id = UUID.randomUUID().toString(),
                storeName = storeName,
                cardNumber = number,
                barcodeFormat = format.uppercase(),
                color = seededColor(storeName)
            )
        )
    }

    fun addCardFromScan(number: String, format: String?) {
        val storeName = guessStoreName(number)
        addCard(storeName = storeName, number = number, format = format ?: "CODE_128")
    }

    private fun seededColor(key: String): Long {
        val palette = listOf(
            0xFFEFF3FF,
            0xFFFFF4E6,
            0xFFE6F7F1,
            0xFFFFEBF3,
            0xFFE8F4FF
        )
        val index = kotlin.math.abs(key.hashCode()) % palette.size
        return palette[index]
    }

    private fun guessStoreName(number: String): String {
        return when {
            number.startsWith("99") -> "Klarna Shop"
            number.startsWith("84") -> "Cinema Club"
            number.length > 11 -> "BioMarket"
            else -> "Nuova carta"
        }
    }
}
