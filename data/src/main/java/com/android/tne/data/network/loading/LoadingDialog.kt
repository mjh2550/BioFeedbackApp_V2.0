package com.android.tne.data.network.loading

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.android.tne.R

/**
 * Created by KimBH on 2022/07/12.
 */
class LoadingDialog {
    private lateinit var mLoadingDialog: Dialog

    companion object {
        private lateinit var _shared: LoadingDialog

        fun shared(): LoadingDialog {
            synchronized(LoadingDialog::class.java) {
                if (Companion::_shared.isInitialized.not())
                    _shared = LoadingDialog()
                }
            return _shared
        }
    }


    fun showLoading(context: Context?) {
        if (context != null) {
            mLoadingDialog = Dialog(context)
            mLoadingDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            // 로딩화면 디자인..
            mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mLoadingDialog.setContentView(R.layout.progressdialog)
            mLoadingDialog.setCancelable(false)

            mLoadingDialog.show()
        }
    }

    fun hideLoading() {
        try {
            if (::mLoadingDialog.isInitialized) {
                if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
