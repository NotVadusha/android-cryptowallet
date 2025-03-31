package com.example.cryptowallet.repository

import com.example.cryptowallet.api.RetrofitClient
import com.example.cryptowallet.model.CryptoCurrencyResponse
import com.example.cryptowallet.util.ApiConfig
import retrofit2.Response

class CryptoRepository {
    
    private val api = RetrofitClient.coinMarketCapApi
    
    suspend fun getCryptoCurrencyPrice(cryptoId: String): Response<CryptoCurrencyResponse> {
        val apiKey = ApiConfig.getCoinMarketCapApiKey()
        return api.getLatestQuote(apiKey = apiKey, id = cryptoId, convert = "USD")
    }
    
    suspend fun getAllCryptoPrices(cryptoIds: List<String>): Response<CryptoCurrencyResponse> {
        val apiKey = ApiConfig.getCoinMarketCapApiKey()
        val idsString = cryptoIds.joinToString(",")
        return api.getLatestQuotes(apiKey = apiKey, ids = idsString, convert = "USD")
    }
}