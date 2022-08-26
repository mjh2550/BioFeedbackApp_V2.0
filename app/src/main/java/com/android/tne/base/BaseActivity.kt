package com.android.tne.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.android.tne.base.response.ResponseResult
import com.android.tne.network.loading.LoadingDialog
import com.android.tne.network.util.ErrorUtil
import com.android.tne.network.util.ResultCodeUtil

/**
 * Created by KimBH on 2022/07/12.
 */

abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel>  : AppCompatActivity(),
    BaseContractView {
    protected lateinit var mDataBinding: DB
    abstract val mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        // LiveData 관찰을 위한 세팅.

        mDataBinding.lifecycleOwner = this

        mViewModel.apply {
            isLoading.observe(this@BaseActivity) {
                if (it) {
                    LoadingDialog.shared().showLoading(this@BaseActivity)
                } else {
                    LoadingDialog.shared().hideLoading()
                }
            }

            resReuslt.observe(this@BaseActivity) {
                loadResultCode(it)
            }

            error.observe(this@BaseActivity) {
                loadErrorMessage(it)
            }
        }

        subscribeUi()
    }

    override fun loadResultCode(resResult: ResponseResult?) {
        ResultCodeUtil.setResult(this@BaseActivity, resResult)
    }

    override fun loadErrorMessage(e: Throwable) {
        ErrorUtil.showErrorMessage(this@BaseActivity, null) {}
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // overridePendingTransition etc..
    }
}