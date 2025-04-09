package com.example.cryptowallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowallet.adapter.CryptoListAdapter
import com.example.cryptowallet.databinding.FragmentCryptoListBinding
import com.example.cryptowallet.model.CryptoCurrencyList

class CryptoListFragment : Fragment() {

    private var _binding: FragmentCryptoListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCryptoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupTrackAssetsButton()
    }
    
    private fun setupRecyclerView() {
        val cryptoAdapter = CryptoListAdapter(
            CryptoCurrencyList.availableCryptos
        ) { selectedCrypto ->
            val action = CryptoListFragmentDirections.actionCryptoListFragmentToCryptoDetailFragment(
                cryptoId = selectedCrypto.id,
                cryptoName = selectedCrypto.name,
                cryptoSymbol = selectedCrypto.symbol
            )
            findNavController().navigate(action)
        }
        
        binding.recyclerviewCrypto.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cryptoAdapter
        }
    }

    private fun setupTrackAssetsButton() {
        binding.buttonTrackAssets.setOnClickListener {
            val action = CryptoListFragmentDirections.actionCryptoListFragmentToAssetsListFragment()
            findNavController().navigate(action)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}