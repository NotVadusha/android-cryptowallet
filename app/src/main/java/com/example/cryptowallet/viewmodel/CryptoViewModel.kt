package com.example.cryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowallet.model.CryptoCurrency
import com.example.cryptowallet.repository.CryptoRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.Date

class CryptoViewModel : ViewModel() {
    
    private val repository = CryptoRepository()
    
    private val _ethereumData = MutableLiveData<EthereumUiState>()
    val ethereumData: LiveData<EthereumUiState> get() = _ethereumData
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error
    
    init {
        fetchEthereumPrice()
    }
    
    fun fetchEthereumPrice() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val response = repository.getCryptoCurrencyPrice("1027") // 1027 is Ethereum's ID
                
                if (response.isSuccessful) {
                    val cryptoResponse = response.body()
                    cryptoResponse?.let {
                        val ethereum = it.data["1027"] // "1027" is Ethereum's ID
                        ethereum?.let { eth ->
                            val usdQuote = eth.quote["USD"]
                            usdQuote?.let { quote ->
                                val formatter = NumberFormat.getCurrencyInstance(Locale.US)
                                val price = formatter.format(quote.price)
                                
                                val percentChange24h = quote.percentChange24h
                                val changeText = String.format("%.2f%%", percentChange24h)
                                val isPositive = percentChange24h >= 0
                                
                                _ethereumData.value = EthereumUiState(
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
    
    data class EthereumUiState(
        val price: String,
        val percentChange24h: String,
        val isPositiveChange: Boolean,
        val lastUpdated: Date
    )
}