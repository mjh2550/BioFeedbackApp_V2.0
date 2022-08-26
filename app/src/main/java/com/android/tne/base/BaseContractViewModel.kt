package com.android.tne.base

import com.android.tne.base.response.ResponseResult

/**
 * Created by KimBH on 2022/07/12.
 */
interface BaseContractViewModel {
    fun observeResultCode(resResult: ResponseResult?)
    fun observeErrorMessage(e: Throwable)
}