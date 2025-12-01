package com.vouchervault.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vouchervault.mobile.data.LoyaltyRepository
import com.vouchervault.mobile.ui.LoyaltyApp
import com.vouchervault.mobile.ui.theme.VoucherVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = LoyaltyRepository()
        setContent {
            VoucherVaultTheme {
                LoyaltyApp(repository)
            }
        }
    }
}
