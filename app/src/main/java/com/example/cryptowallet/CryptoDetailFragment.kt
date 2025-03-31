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
import androidx.navigation.fragment.navArgs
import com.example.cryptowallet.databinding.FragmentCryptoDetailBinding
import com.example.cryptowallet.viewmodel.CryptoDetailViewModel
import com.example.cryptowallet.viewmodel.CryptoDetailViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CryptoDetailFragment : Fragment() {

    private var _binding: FragmentCryptoDetailBinding? = null
    private val binding get() = _binding!!
    private val args: CryptoDetailFragmentArgs by navArgs()
    
    private val viewModel: CryptoDetailViewModel by viewModels { 
        CryptoDetailViewModelFactory(
            args.cryptoId,
            args.cryptoName,
            args.cryptoSymbol
        )
    }
    
    private var autoUpdateJob: Job? = null
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCryptoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Set crypto name and symbol immediately
        binding.textviewTitle.text = args.cryptoName
        binding.textviewSymbol.text = args.cryptoSymbol
        
        setupObservers()
        setupListeners()
        startAutoUpdates()
    }
    
    private fun setupObservers() {
        viewModel.cryptoData.observe(viewLifecycleOwner) { cryptoData ->
            binding.textviewPrice.text = cryptoData.price
            binding.textviewPriceChange.text = cryptoData.percentChange24h
            
            // Set color based on price change
            val textColor = if (cryptoData.isPositiveChange) {
                ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            } else {
                ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
            }
            binding.textviewPriceChange.setTextColor(textColor)
            
            // Format the date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(cryptoData.lastUpdated)
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
            viewModel.fetchCryptoPrice()
        }
    }
    
    private fun startAutoUpdates() {
        autoUpdateJob?.cancel()
        autoUpdateJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (isActive) {
                    // Calculate time until next update (5 seconds past the next minute)
                    val calendar = Calendar.getInstance()
                    val currentSeconds = calendar.get(Calendar.SECOND)
                    val currentMillis = calendar.get(Calendar.MILLISECOND)
                    
                    // Calculate how many milliseconds until the next :05 seconds
                    val delayMillis = if (currentSeconds < 5) {
                        // We're before :05 in this minute, calculate time to the next :05
                        ((5 - currentSeconds) * 1000) - currentMillis
                    } else {
                        // We're after :05 in this minute, calculate time to :05 of next minute
                        ((65 - currentSeconds) * 1000) - currentMillis
                    }
                    
                    // Calculate next update time
                    val nextUpdateCalendar = Calendar.getInstance()
                    nextUpdateCalendar.add(Calendar.MILLISECOND, delayMillis.toInt())
                    val nextUpdateTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .format(nextUpdateCalendar.time)
                    
                    // Update the next update text
                    binding.textviewNextUpdate.text = "Auto-updates at 5 seconds past every minute (Next: $nextUpdateTime)"
                    
                    // For more precise countdown, update every second until the target time
                    val secondsToWait = (delayMillis / 1000).toLong()
                    for (i in secondsToWait downTo 1L) {
                        if (!isActive) break
                        delay(1000L)
                        binding.textviewNextUpdate.text = "Auto-updates at 5 seconds past every minute (Next: $nextUpdateTime, ${i}s remaining)"
                    }
                    
                    // Wait for the remaining milliseconds (less than a second)
                    val remainingMillis = (delayMillis % 1000).toLong()
                    if (remainingMillis > 0) {
                        delay(remainingMillis)
                    }
                    
                    // Perform the update
                    viewModel.fetchCryptoPrice()
                    
                    // Log update for debugging
                    val updateTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .format(Calendar.getInstance().time)
                    println("Auto-updated at $updateTime")
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