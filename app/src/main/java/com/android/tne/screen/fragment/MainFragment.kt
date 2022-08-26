package com.android.tne.screen.fragment

import androidx.fragment.app.activityViewModels
import com.android.tne.R
import com.android.tne.base.BaseFragment
import com.android.tne.databinding.FragmentMainBinding
import com.android.tne.screen.viewmodel.MainViewModel

/**
 * Created by KimBH on 2022/07/12.
 */
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {
    override val mViewModel by activityViewModels<MainViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun subscribeUi() {
        with(mViewModel) {

        }
        with(mDataBinding) {
            vm = mViewModel
        }
    }
}