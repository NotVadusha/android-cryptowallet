package com.example.cryptowallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.ItemAssetBinding
import com.example.cryptowallet.model.AssetType
import com.example.cryptowallet.model.UserAsset
import java.text.NumberFormat
import java.util.Locale

class AssetListAdapter : ListAdapter<UserAsset, AssetListAdapter.AssetViewHolder>(AssetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val binding = ItemAssetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AssetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = getItem(position)
        holder.bind(asset)
    }

    class AssetViewHolder(private val binding: ItemAssetBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(asset: UserAsset) {
            binding.apply {
                textviewAssetName.text = asset.assetName
                
                val statusText = if (asset.isActive) "Active" else "Inactive"
                val statusColor = if (asset.isActive) {
                    R.color.crypto_background_nav
                } else {
                    android.R.color.darker_gray
                }
                textviewAssetStatus.text = statusText
                textviewAssetStatus.backgroundTintList = ContextCompat.getColorStateList(
                    itemView.context, statusColor
                )
                
                textviewAssetType.text = when (asset.assetType) {
                    AssetType.CRYPTOCURRENCY -> "Cryptocurrency"
                    AssetType.STOCK -> "Stock"
                    AssetType.COMMODITY -> "Commodity"
                }
                
                val numberFormat = NumberFormat.getNumberInstance(Locale.US)
                textviewAmountValue.text = numberFormat.format(asset.assetAmount)
                
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
                textviewPriceValue.text = currencyFormat.format(asset.assetPurchasePrice)
                
                progressbarRisk.progress = asset.riskLevel
            }
        }
    }
    
    private class AssetDiffCallback : DiffUtil.ItemCallback<UserAsset>() {
        override fun areItemsTheSame(oldItem: UserAsset, newItem: UserAsset): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: UserAsset, newItem: UserAsset): Boolean {
            return oldItem == newItem
        }
    }
}