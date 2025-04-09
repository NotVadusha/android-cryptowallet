package com.example.cryptowallet.util

import android.content.Context
import android.content.SharedPreferences
import com.example.cryptowallet.model.AssetType
import com.example.cryptowallet.model.UserAsset
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AssetPrefsManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    private val gson = Gson()
    
    fun saveAsset(asset: UserAsset) {
        val assets = getAllAssets().toMutableList()
        
        val assetToSave = if (asset.id == 0L) {
            val newId = System.currentTimeMillis()
            asset.copy(id = newId)
        } else {
            asset
        }
        
        val existingAssetIndex = assets.indexOfFirst { it.assetName.equals(assetToSave.assetName, ignoreCase = true) }
        
        if (existingAssetIndex >= 0) {
            val existingAsset = assets[existingAssetIndex]
            val updatedAsset = existingAsset.copy(
                assetAmount = existingAsset.assetAmount + assetToSave.assetAmount,
                assetPurchasePrice = ((existingAsset.assetAmount * existingAsset.assetPurchasePrice) + 
                                     (assetToSave.assetAmount * assetToSave.assetPurchasePrice)) / 
                                     (existingAsset.assetAmount + assetToSave.assetAmount),
                isActive = existingAsset.isActive || assetToSave.isActive,
                assetType = existingAsset.assetType,
                riskLevel = existingAsset.riskLevel
            )
            assets[existingAssetIndex] = updatedAsset
        } else {
            assets.add(assetToSave)
        }
        
        val json = gson.toJson(assets)
        sharedPreferences.edit().putString(ASSETS_KEY, json).apply()
    }
    
    fun getAllAssets(): List<UserAsset> {
        val json = sharedPreferences.getString(ASSETS_KEY, "")
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        
        val type: Type = object : TypeToken<List<UserAsset>>() {}.type
        return gson.fromJson(json, type)
    }
    
    companion object {
        private const val PREFS_NAME = "asset_prefs"
        private const val ASSETS_KEY = "assets_list"
    }
}