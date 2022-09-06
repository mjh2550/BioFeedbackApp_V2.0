package com.android.tne.screen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.tne.base.BaseViewModel
import com.android.tne.base.response.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by KimBH on 2022/07/12.
 */
@HiltViewModel
class MainViewModel @Inject constructor () : BaseViewModel() {

    val testst = MutableLiveData("testst")
   /* val testStr = MutableLiveData("testLiveStr")

    fun click(){
        testStr.value = "clicked"
        viewModelScope.launch {

            isLoading.value = true

            delay(5000)

            isLoading.value = false
        }
    }*/
}