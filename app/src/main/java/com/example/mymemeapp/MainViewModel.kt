package com.example.mymemeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemeapp.data.repository.MemeRepository
import com.example.mymemeapp.models.AllMems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MemeRepository
) :  ViewModel() {

    private val _allMeme = MutableLiveData<AllMems>()
    val allMeme: LiveData<AllMems>
        get() = _allMeme

    fun getAllMeme() {
        viewModelScope.launch {
            repository.getAllMemes().let {
                if (it.isSuccessful) {
                    _allMeme.postValue(it.body())
                }
                else {
                    Log.d("checkData", "failed ${it.errorBody()}")
                }
            }

        }
    }



}
