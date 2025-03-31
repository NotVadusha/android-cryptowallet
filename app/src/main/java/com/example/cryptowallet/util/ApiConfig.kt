package com.example.cryptowallet.util

import com.example.cryptowallet.BuildConfig

/**
 * Helper class to securely manage API keys and configuration
 */
object ApiConfig {
    /**
     * Get API key from BuildConfig, which sources from environment variables or gradle.properties
     */
    fun getCoinMarketCapApiKey(): String {
        return BuildConfig.COINMARKETCAP_API_KEY
    }
}