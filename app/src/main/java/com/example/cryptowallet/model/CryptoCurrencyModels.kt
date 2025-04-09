package com.example.cryptowallet.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CryptoCurrencyResponse(
    val status: Status,
    val data: Map<String, CryptoCurrency>
)

data class CryptoMetadataResponse(
    val status: Status,
    val data: Map<String, CryptoMetadata>
)

data class Status(
    val timestamp: Date,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("error_message")
    val errorMessage: String?,
    val elapsed: Int,
    @SerializedName("credit_count")
    val creditCount: Int
)

data class CryptoCurrency(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerializedName("num_market_pairs")
    val numMarketPairs: Int,
    @SerializedName("date_added")
    val dateAdded: Date,
    val tags: List<String>,
    @SerializedName("max_supply")
    val maxSupply: Long?,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("total_supply")
    val totalSupply: Double,
    @SerializedName("infinite_supply")
    val infiniteSupply: Boolean,
    @SerializedName("cmc_rank")
    val cmcRank: Int,
    @SerializedName("last_updated")
    val lastUpdated: Date,
    val quote: Map<String, Quote>
)

data class CryptoMetadata(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val category: String,
    val description: String,
    @SerializedName("date_added")
    val dateAdded: Date,
    @SerializedName("date_launched")
    val dateLaunched: Date?,
    val tags: List<String>,
    val urls: CryptoUrls,
    val logo: String,
    @SerializedName("is_hidden")
    val isHidden: Int
)

data class CryptoUrls(
    val website: List<String>,
    val twitter: List<String>,
    val reddit: List<String>,
    @SerializedName("message_board")
    val messageBoard: List<String>,
    @SerializedName("source_code")
    val sourceCode: List<String>,
    val chat: List<String>,
    val explorer: List<String>,
    val facebook: List<String>,
    val technical_doc: List<String>,
    val announcement: List<String>
)

data class Quote(
    val price: Double,
    @SerializedName("volume_24h")
    val volume24h: Double,
    @SerializedName("volume_change_24h")
    val volumeChange24h: Double,
    @SerializedName("percent_change_1h")
    val percentChange1h: Double,
    @SerializedName("percent_change_24h")
    val percentChange24h: Double,
    @SerializedName("percent_change_7d")
    val percentChange7d: Double,
    @SerializedName("percent_change_30d")
    val percentChange30d: Double,
    @SerializedName("percent_change_60d")
    val percentChange60d: Double,
    @SerializedName("percent_change_90d")
    val percentChange90d: Double,
    @SerializedName("market_cap")
    val marketCap: Double,
    @SerializedName("market_cap_dominance")
    val marketCapDominance: Double,
    @SerializedName("fully_diluted_market_cap")
    val fullyDilutedMarketCap: Double,
    @SerializedName("last_updated")
    val lastUpdated: Date
)