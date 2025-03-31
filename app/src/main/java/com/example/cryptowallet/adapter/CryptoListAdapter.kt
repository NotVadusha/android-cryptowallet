package com.example.cryptowallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.databinding.ItemCryptoCurrencyBinding
import com.example.cryptowallet.model.CryptoListItem

class CryptoListAdapter(
    private val cryptoList: List<CryptoListItem>,
    private val onItemClick: (CryptoListItem) -> Unit
) : RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder>() {

    inner class CryptoViewHolder(private val binding: ItemCryptoCurrencyBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(crypto: CryptoListItem) {
            binding.textCryptoName.text = crypto.name
            binding.textCryptoSymbol.text = crypto.symbol
            
            // Set click listener
            binding.root.setOnClickListener {
                onItemClick(crypto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ItemCryptoCurrencyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(cryptoList[position])
    }

    override fun getItemCount(): Int = cryptoList.size
}