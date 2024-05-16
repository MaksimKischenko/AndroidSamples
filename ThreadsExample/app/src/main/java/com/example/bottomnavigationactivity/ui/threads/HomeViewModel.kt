package com.example.bottomnavigationactivity.ui.threads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThreadsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is current Time view"
    }
    val text: LiveData<String> = _text
}