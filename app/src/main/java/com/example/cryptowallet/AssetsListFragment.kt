package com.example.cryptowallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptowallet.adapter.AssetListAdapter
import com.example.cryptowallet.databinding.FragmentAssetsListBinding
import com.example.cryptowallet.viewmodel.AssetFormViewModel

class AssetsListFragment : Fragment() {
    
    private var _binding: FragmentAssetsListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AssetFormViewModel
    private lateinit var assetAdapter: AssetListAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssetsListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[AssetFormViewModel::class.java]
        
        setupRecyclerView()
        setupAddButton()
        observeAssets()
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadAssets()
    }
    
    private fun setupRecyclerView() {
        assetAdapter = AssetListAdapter()
        binding.recyclerviewAssets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = assetAdapter
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadAssets()
        }
    }
    
    private fun setupAddButton() {
        binding.buttonAddAsset.setOnClickListener {
            findNavController().navigate(
                AssetsListFragmentDirections.actionAssetsListFragmentToAssetFormFragment()
            )
        }
    }
    
    private fun observeAssets() {
        viewModel.assets.observe(viewLifecycleOwner) { assets ->
            assetAdapter.submitList(assets)
            
            binding.swipeRefresh.isRefreshing = false
            
            if (assets.isEmpty()) {
                binding.textviewNoAssets.visibility = View.VISIBLE
                binding.recyclerviewAssets.visibility = View.GONE
            } else {
                binding.textviewNoAssets.visibility = View.GONE
                binding.recyclerviewAssets.visibility = View.VISIBLE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}