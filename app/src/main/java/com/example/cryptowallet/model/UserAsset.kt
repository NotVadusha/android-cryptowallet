package com.example.cryptowallet.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAsset(
    val id: Long = 0,
    val assetName: String,
    val assetAmount: Double,
    val assetPurchasePrice: Double,
    val isActive: Boolean,
    val assetType: AssetType,
    val riskLevel: Int // 0-100 from SeekBar
) : Parcelable

enum class AssetType {
    CRYPTOCURRENCY,
    STOCK,
    COMMODITY
}