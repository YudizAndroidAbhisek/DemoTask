package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: NewsRepository= NewsRepository()) : ViewModel(){

    val message :MutableLiveData<Home> = MutableLiveData()


    fun sendMessage(name: String,emailId: String,address: String,phone: String,hobby: String) {
        message.value = Home(name,emailId,address,phone,hobby)
    }

    val _topicData = MutableLiveData<List<Articles>>()
    val topicList = _topicData

    fun data(topicName : String) {
        viewModelScope.launch {
            _topicData.value = repository.fetchData(topicName)
        }
    }


}