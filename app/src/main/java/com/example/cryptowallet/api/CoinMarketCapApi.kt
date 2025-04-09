package com.example.cryptowallet.api

import com.example.cryptowallet.model.CryptoCurrencyResponse
import com.example.cryptowallet.model.CryptoMetadataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinMarketCapApi {
    
    @GET("v1/cryptocurrency/quotes/latest")
    suspend fun getLatestQuote(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("id") id: String, 
        @Query("convert") convert: String = "USD"
    ): Response<CryptoCurrencyResponse>
    
    @GET("v1/cryptocurrency/quotes/latest")
    suspend fun getLatestQuotes(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("id") ids: String, 
        @Query("convert") convert: String = "USD"
    ): Response<CryptoCurrencyResponse>
    
    @GET("v2/cryptocurrency/info")
    suspend fun getCryptoMetadata(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("id") id: String
    ): Response<CryptoMetadataResponse>
}