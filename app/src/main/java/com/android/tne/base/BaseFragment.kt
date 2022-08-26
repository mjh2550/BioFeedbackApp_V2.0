package com.android.tne.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Created by KimBH on 2022/07/12.
 */
abstract class BaseFragment<DB: ViewDataBinding, VM: BaseViewModel> : Fragment() {
    protected lateinit var mDataBinding: DB
    abstract val mViewModel: VM

    fun isInitialized() = ::mDataBinding.isInitialized

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        mDataBinding.lifecycleOwner = this

        subscribeUi()

        return mDataBinding.root
    }

    abstract fun getLayoutRes(): Int
    abstract fun subscribeUi()
}