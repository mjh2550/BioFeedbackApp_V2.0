package com.android.tne.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.tne.base.BaseViewModel
import com.android.tne.domain.usecase.FetchTestStrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by KimBH on 2022/07/12.
 */
@HiltViewModel
class MainViewModel @Inject constructor (
    private val fetchTestStrUseCase: FetchTestStrUseCase
        ) : BaseViewModel() {

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

    init {
        viewModelScope.launch {
            delay(3000)
            testLoad()

        }
    }

    private fun testLoad() {
        testst.value = fetchTestStrUseCase.invoke()
    }
}