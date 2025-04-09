package com.example.cryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptowallet.repository.CryptoRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

class CryptoDetailViewModel(
    private val cryptoId: String,
    private val cryptoName: String,
    private val cryptoSymbol: String
) : ViewModel() {
    
    private val repository = CryptoRepository()
    
    private val _cryptoData = MutableLiveData<CryptoUiState>()
    val cryptoData: LiveData<CryptoUiState> get() = _cryptoData
    
    private val _websiteUrl = MutableLiveData<String?>()
    val websiteUrl: LiveData<String?> get() = _websiteUrl
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error
    
    init {
        fetchCryptoPrice()
        fetchCryptoMetadata()
    }
    
    fun fetchCryptoPrice() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val response = repository.getCryptoCurrencyPrice(cryptoId)
                
                if (response.isSuccessful) {
                    val cryptoResponse = response.body()
                    cryptoResponse?.let {
                        val crypto = it.data[cryptoId]
                        crypto?.let { cryptoCurrency ->
                            val usdQuote = cryptoCurrency.quote["USD"]
                            usdQuote?.let { quote ->
                                val formatter = NumberFormat.getCurrencyInstance(Locale.US)
                                val price = formatter.format(quote.price)
                                
                                val percentChange24h = quote.percentChange24h
                                val changeText = String.format("%.2f%%", percentChange24h)
                                val isPositive = percentChange24h >= 0
                                
                                _cryptoData.value = CryptoUiState(
                                    name = cryptoName,
                                    symbol = cryptoSymbol,
                                    price = price,
                                    percentChange24h = changeText,
                                    isPositiveChange = isPositive,
                                    lastUpdated = quote.lastUpdated
                                )
                            }
                        }
                    }
                } else {
                    _error.value = "Error: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun fetchCryptoMetadata() {
        viewModelScope.launch {
            try {
                val response = repository.getCryptoMetadata(cryptoId)
                
                if (response.isSuccessful) {
                    val metadataResponse = response.body()
                    metadataResponse?.let {
                        val cryptoMetadata = it.data[cryptoId]
                        cryptoMetadata?.let { metadata ->
                            if (metadata.urls.website.isNotEmpty()) {
                                _websiteUrl.value = metadata.urls.website[0]
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Silently fail for metadata - we don't want to block price display
                _websiteUrl.value = null
            }
        }
    }
    
    data class CryptoUiState(
        val name: String,
        val symbol: String,
        val price: String,
        val percentChange24h: String,
        val isPositiveChange: Boolean,
        val lastUpdated: Date
    )
}

class CryptoDetailViewModelFactory(
    private val cryptoId: String,
    private val cryptoName: String,
    private val cryptoSymbol: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CryptoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CryptoDetailViewModel(cryptoId, cryptoName, cryptoSymbol) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}