package com.android.tne.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.android.tne.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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