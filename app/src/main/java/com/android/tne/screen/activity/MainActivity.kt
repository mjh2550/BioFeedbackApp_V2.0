package com.android.tne.screen.activity

import androidx.activity.viewModels
import com.android.tne.base.BaseActivity
import com.android.tne.R
import com.android.tne.databinding.ActivityMainBinding
import com.android.tne.screen.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity  @Inject constructor(
): BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val mViewModel: MainViewModel by viewModels()

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun subscribeUi() {
        with(mViewModel) {

        }
        with(mDataBinding) {
            vm = mViewModel
        }
    }
}