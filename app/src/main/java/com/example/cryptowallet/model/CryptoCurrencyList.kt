package com.example.cryptowallet.model

data class CryptoListItem(
    val id: String,
    val name: String,
    val symbol: String,
    val iconUrl: String? = null
)

/**
 * Predefined list of cryptocurrencies we want to display
 */
object CryptoCurrencyList {
    val availableCryptos = listOf(
        CryptoListItem(
            id = "1", // CoinMarketCap ID for Bitcoin
            name = "Bitcoin",
            symbol = "BTC"
        ),
        CryptoListItem(
            id = "1027", // CoinMarketCap ID for Ethereum
            name = "Ethereum",
            symbol = "ETH"
        ),
        CryptoListItem(
            id = "1958", // CoinMarketCap ID for TRON
            name = "TRON",
            symbol = "TRX"
        )
    )
}