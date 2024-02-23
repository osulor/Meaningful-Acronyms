package com.example.meaningfulacronyms.ui

import android.app.Activity
import android.content.Context
import android.health.connect.datatypes.units.Length
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meaningfulacronyms.R
import com.example.meaningfulacronyms.databinding.ActivityMainBinding
import com.example.meaningfulacronyms.model.Acromine
import com.example.meaningfulacronyms.network.AcromineRepository
import com.example.meaningfulacronyms.util.ApiCallState
import com.example.meaningfulacronyms.vm.AcromineViewModel
import com.example.meaningfulacronyms.vm.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.Duration

class MainActivity : AppCompatActivity() {

    private lateinit var acromineViewModel: AcromineViewModel
    private lateinit var wordMeaningListAdapter: WordMeaningListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory(AcromineRepository())
        acromineViewModel = ViewModelProvider(this, viewModelFactory)[AcromineViewModel::class.java]

        binding.searchButton.setOnClickListener {
            val isNetworkAvailable =isNetworkAvailable(this@MainActivity)
            if (isNetworkAvailable){
                lifecycleScope.launch { retrieveData(binding.wordEntry.text.toString()) }
             } else {
                Toast.makeText(this, "NO INTERNET, CHECK CONNECTIVITY", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpRecyclerView() {
        wordMeaningListAdapter = WordMeaningListAdapter()
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.meaningsRv.apply {
            adapter = wordMeaningListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.meaningsRv.addItemDecoration(itemDecoration)
    }

    private suspend fun retrieveData(word: String) {
        acromineViewModel.getMeanings(word)
        setUpRecyclerView()
        acromineViewModel.wordMeanings.observe(this, Observer { response ->

            when (response) {
                is ApiCallState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this, "The data is being retrieved", Toast.LENGTH_LONG).show()
                }

                is ApiCallState.Success -> {
                    response.data?.let { acromineResponse ->
                        displayData(acromineResponse)
                    }
                }

                is ApiCallState.Error -> {
                    response.message?.let { errorMessage ->
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun displayData(acromineResponse: List<Acromine>) {
        binding.progressBar.visibility = View.GONE
        if (acromineResponse.isNotEmpty()) {
            wordMeaningListAdapter.differ.submitList(acromineResponse[0].lfs)
            showRecyclerViewData()
        } else {
            hideRecyclerViewData()
        }
    }

    private fun hideRecyclerViewData(){
        binding.meaningsRv.visibility = View.GONE
        binding.infoTextView.visibility = View.VISIBLE
    }

    private fun showRecyclerViewData(){
        binding.meaningsRv.visibility = View.VISIBLE
        binding.infoTextView.visibility = View.GONE
    }

    private fun isNetworkAvailable(activity: Activity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val networkInfo = connectivityManager.getNetworkCapabilities(networkCapabilities)
            return networkInfo?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}

