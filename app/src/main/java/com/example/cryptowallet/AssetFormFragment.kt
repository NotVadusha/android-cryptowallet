package com.example.cryptowallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cryptowallet.databinding.FragmentAssetFormBinding
import com.example.cryptowallet.model.AssetType
import com.example.cryptowallet.viewmodel.AssetFormViewModel

class AssetFormFragment : Fragment() {
    
    private var _binding: FragmentAssetFormBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AssetFormViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssetFormBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[AssetFormViewModel::class.java]
        
        setupRiskSeekBar()
        setupSaveButton()
    }
    
    private fun setupRiskSeekBar() {
        binding.seekbarRisk.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textviewRiskValue.text = "$progress%"
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
    
    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            if (validateForm()) {
                saveAsset()
            }
        }
    }
    
    private fun validateForm(): Boolean {
        val assetName = binding.edittextAssetName.text.toString().trim()
        val assetAmount = binding.edittextAssetAmount.text.toString().trim()
        val assetPrice = binding.edittextPurchasePrice.text.toString().trim()
        
        if (assetName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter asset name", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (assetAmount.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter asset amount", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (assetPrice.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter purchase price", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }
    
    private fun saveAsset() {
        val assetName = binding.edittextAssetName.text.toString().trim()
        val assetAmount = binding.edittextAssetAmount.text.toString().toDouble()
        val assetPrice = binding.edittextPurchasePrice.text.toString().toDouble()
        val isActive = binding.checkboxActive.isChecked
        val riskLevel = binding.seekbarRisk.progress
        
        val assetType = when (binding.radiogroupAssetType.checkedRadioButtonId) {
            R.id.radiobutton_crypto -> AssetType.CRYPTOCURRENCY
            R.id.radiobutton_stock -> AssetType.STOCK
            R.id.radiobutton_commodity -> AssetType.COMMODITY
            else -> AssetType.CRYPTOCURRENCY
        }
        
        viewModel.saveAsset(
            assetName = assetName,
            assetAmount = assetAmount,
            assetPurchasePrice = assetPrice,
            isActive = isActive,
            assetType = assetType,
            riskLevel = riskLevel
        )
        
        Toast.makeText(requireContext(), "Asset saved successfully", Toast.LENGTH_SHORT).show()
        viewModel.loadAssets()
        findNavController().navigateUp()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}