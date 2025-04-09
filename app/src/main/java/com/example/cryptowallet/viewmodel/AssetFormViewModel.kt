package com.example.cryptowallet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptowallet.model.AssetType
import com.example.cryptowallet.model.UserAsset
import com.example.cryptowallet.util.AssetPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssetFormViewModel(application: Application) : AndroidViewModel(application) {
    
    private val prefsManager = AssetPrefsManager(application)
    
    private val _assets = MutableLiveData<List<UserAsset>>()
    val assets: LiveData<List<UserAsset>> = _assets
    
    init {
        loadAssets()
    }
    
    fun loadAssets() {
        viewModelScope.launch {
            val assetList = withContext(Dispatchers.IO) {
                prefsManager.getAllAssets()
            }
            _assets.value = assetList
        }
    }
    
    fun saveAsset(
        assetName: String,
        assetAmount: Double,
        assetPurchasePrice: Double,
        isActive: Boolean,
        assetType: AssetType,
        riskLevel: Int
    ) {
        val asset = UserAsset(
            assetName = assetName,
            assetAmount = assetAmount,
            assetPurchasePrice = assetPurchasePrice,
            isActive = isActive,
            assetType = assetType,
            riskLevel = riskLevel
        )
        
        viewModelScope.launch(Dispatchers.IO) {
            prefsManager.saveAsset(asset)

            withContext(Dispatchers.Main) {
                loadAssets()
            }
        }
    }
}