package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _countries = MutableLiveData<List<CountryInfo>>()
    val countries: LiveData<List<CountryInfo>> = _countries

    private val _isCountriesLoading = MutableLiveData<Boolean>()
    val isCountriesLoading: LiveData<Boolean> = _isCountriesLoading

    private val apiKey = "01171f7a5e48d1f7f777848d695252bd"
    private var currentCountry = "us"
    private var currentCategory = "general"

    init {
        fetchCountries()
        fetchTopHeadlines()
    }

    fun fetchCountries() {
        _isCountriesLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.countriesApi.getAllCountries()
                val sortedCountries = response
                    .filter { it.name.common.isNotBlank() && it.cca2.isNotBlank() }
                    .map { CountryInfo(name = it.name.common, code = it.cca2.lowercase()) }
                    .sortedBy { it.name }
                _countries.value = sortedCountries
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load countries: ${e.message}"
            } finally {
                _isCountriesLoading.value = false
            }
        }
    }

    fun fetchTopHeadlines(country: String = currentCountry, category: String = currentCategory) {
        currentCountry = country
        currentCategory = category
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.newsApi.getTopHeadlines(
                    country = country,
                    category = category,
                    apiKey = apiKey
                )
                _articles.value = response.articles
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to fetch news"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchNews(query: String) {
        if (query.isBlank()) {
            fetchTopHeadlines(currentCountry, currentCategory)
            return
        }

        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.newsApi.searchNews(
                    query = query,
                    country = currentCountry,
                    apiKey = apiKey
                )
                _articles.value = response.articles
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Search failed"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
