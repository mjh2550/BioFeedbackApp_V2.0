package com.android.tne.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.tne.base.BaseViewModel
import com.android.tne.domain.model.Test
import com.android.tne.domain.usecase.FetchBoardGetAllListUseCase
import com.android.tne.domain.usecase.FetchTestStrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

/**
 * Created by KimBH on 2022/07/12.
 */
@HiltViewModel
class MainViewModel @Inject constructor (
    private val fetchTestStrUseCase: FetchTestStrUseCase,
    private val fetchBoardGetAllListUseCase: FetchBoardGetAllListUseCase
        ) : BaseViewModel() {

    val testst = MutableLiveData("testst")
    val boardList = MutableLiveData<ArrayList<Test>>()
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
          /*  getAllList(
                successCallBack = {response ->
                    val result = response.body()!!
                    if(response.isSuccessful) {
                        for (r in result) {
                            Log.d("test", "For : $r")
                        }
                        boardList.value = result
                        Log.d("Test", "OnRequest Success : $result")
                    } else {
                        Log.d("Test", "OnRequest Fail : $result")
                    }
                },
                failCallBack = {
                   Log.d("Test", "On Fail : ${it.message}")
                }
            )*/
        }
    }

    private fun testLoad() {
        testst.value = fetchTestStrUseCase.invoke()
    }

    private fun getAllList(
        successCallBack : ( response: Response<ArrayList<Test>>) -> Unit,
        failCallBack : (Throwable) -> Unit,
    ) {
        return  fetchBoardGetAllListUseCase.invoke().enqueue(
            object : retrofit2.Callback<ArrayList<Test>>{
                //callback 처리
                override fun onResponse(
                    call: Call<ArrayList<Test>>,
                    response: Response<ArrayList<Test>>
                ) { successCallBack(response)}

                override fun onFailure(call: Call<ArrayList<Test>>, t: Throwable) {
                    failCallBack(t)
                }

            }
        )
    }
}