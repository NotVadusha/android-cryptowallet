package com.example.cryptowallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cryptowallet.databinding.FragmentFirstBinding
import com.example.cryptowallet.viewmodel.CryptoViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CryptoViewModel by viewModels()
    private var autoUpdateJob: Job? = null
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupListeners()
        startAutoUpdates()
    }
    
    private fun setupObservers() {
        viewModel.ethereumData.observe(viewLifecycleOwner) { ethData ->
            binding.textviewPrice.text = ethData.price
            binding.textviewPriceChange.text = ethData.percentChange24h
            
            val textColor = if (ethData.isPositiveChange) {
                ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            } else {
                ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
            }
            binding.textviewPriceChange.setTextColor(textColor)
            
            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(ethData.lastUpdated)
            binding.textviewLastUpdated.text = "Last updated: $formattedDate"
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.textviewPrice.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
        
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun setupListeners() {
        binding.buttonRefresh.setOnClickListener {
            viewModel.fetchEthereumPrice()
        }
    }
    
    private fun startAutoUpdates() {
        autoUpdateJob?.cancel()
        autoUpdateJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (isActive) {
                    val calendar = Calendar.getInstance()
                    val currentSeconds = calendar.get(Calendar.SECOND)
                    val currentMillis = calendar.get(Calendar.MILLISECOND)
                    
                    val delayMillis = if (currentSeconds < 5) {
                        ((5 - currentSeconds) * 1000) - currentMillis
                    } else {
                        ((65 - currentSeconds) * 1000) - currentMillis
                    }
                    
                    val nextUpdateCalendar = Calendar.getInstance()
                    nextUpdateCalendar.add(Calendar.MILLISECOND, delayMillis.toInt())
                    val nextUpdateTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .format(nextUpdateCalendar.time)
                    
                    binding.textviewNextUpdate.text = "Auto-updates at 5 seconds past every minute (Next: $nextUpdateTime)"
                    
                    val secondsToWait = (delayMillis / 1000).toLong()
                    for (i in secondsToWait downTo 1L) {
                        if (!isActive) break
                        delay(1000L)
                        binding.textviewNextUpdate.text = "Auto-updates at 5 seconds past every minute (Next: $nextUpdateTime, ${i}s remaining)"
                    }
                    
                    val remainingMillis = (delayMillis % 1000).toLong()
                    if (remainingMillis > 0) {
                        delay(remainingMillis)
                    }
                    
                    viewModel.fetchEthereumPrice()
                    
                    val updateTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .format(Calendar.getInstance().time)
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        autoUpdateJob?.cancel()
        _binding = null
    }
}