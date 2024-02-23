package com.example.meaningfulacronyms.ui

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
import com.example.meaningfulacronyms.network.AcromineRepository
import com.example.meaningfulacronyms.util.ApiCallState
import com.example.meaningfulacronyms.vm.AcromineViewModel
import com.example.meaningfulacronyms.vm.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var acromineViewModel:  AcromineViewModel
    private lateinit var wordMeaningListAdapter: WordMeaningListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var meaningsRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelFactory = ViewModelFactory(AcromineRepository())
        acromineViewModel = ViewModelProvider(this,viewModelFactory)[AcromineViewModel::class.java]

        val searchButton = findViewById<Button>(R.id.search_button)
        val word_entry = findViewById<EditText>(R.id.word_entry)

        meaningsRv = findViewById(R.id.meaningsRv)

        searchButton.setOnClickListener {
            lifecycleScope.launch { retrieveData(word_entry.text.toString()) }
        }

    }

    private fun setUpRecyclerView(){
        wordMeaningListAdapter = WordMeaningListAdapter()
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        meaningsRv.apply {
            adapter = wordMeaningListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        meaningsRv.addItemDecoration(itemDecoration)
    }

    private suspend fun retrieveData(word: String){
        progressBar = findViewById(R.id.progress_bar)
        acromineViewModel.getMeanings(word)
        setUpRecyclerView()
        acromineViewModel.wordMeanings.observe(this, Observer {response ->

            when (response){
                is ApiCallState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    Toast.makeText(this,"The data is being retrieved", Toast.LENGTH_LONG).show()
                }
                is ApiCallState.Success -> {
                    response.data?.let {acromineResponse ->
                        progressBar.visibility = View.GONE
                        wordMeaningListAdapter.differ.submitList(acromineResponse[0].lfs)
                        meaningsRv.visibility = View.VISIBLE

                    }
                }
                is ApiCallState.Error -> {
                    response.message?.let { errorMessage ->
                        progressBar.visibility = View.GONE
                        Toast.makeText(this,errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}