package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter
    
    private val countries = mapOf(
        "United States" to "us",
        "Pakistan" to "pk",
        "United Kingdom" to "gb",
        "India" to "in",
        "Saudi Arabia" to "sa",
        "UAE" to "ae"
    )

    private val categories = listOf(
        "general", "world", "nation", "business", "technology", 
        "entertainment", "sports", "science", "health"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSpinners()
        setupSearch()
        observeViewModel()

        binding.btnRefresh.setOnClickListener {
            refreshNews()
        }
        
        // Initial fetch
        refreshNews()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
        }
    }

    private fun setupSpinners() {
        // Country Spinner
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries.keys.toList())
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countrySpinner.adapter = countryAdapter

        // Category Spinner
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = categoryAdapter

        val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                refreshNews()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.countrySpinner.onItemSelectedListener = itemSelectedListener
        binding.categorySpinner.onItemSelectedListener = itemSelectedListener
    }

    private fun setupSearch() {
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString()
                viewModel.searchNews(query)
                true
            } else {
                false
            }
        }
        
        binding.searchLayout.setEndIconOnClickListener {
            val query = binding.searchEditText.text.toString()
            viewModel.searchNews(query)
        }
    }

    private fun refreshNews() {
        val countryName = binding.countrySpinner.selectedItem?.toString() ?: "United States"
        val countryCode = countries[countryName] ?: "us"
        val category = binding.categorySpinner.selectedItem?.toString() ?: "general"
        viewModel.fetchTopHeadlines(countryCode, category)
    }

    private fun observeViewModel() {
        viewModel.articles.observe(this) { articles ->
            newsAdapter.setArticles(articles)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                showError(message)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.errorText.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = "Error: $message"
    }
}
