package com.android.tne.screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.tne.R
import com.android.tne.base.BaseFragment
import com.android.tne.databinding.FragmentSettingBinding
import com.android.tne.screen.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class SettingFragment : BaseFragment<FragmentSettingBinding,MainViewModel>() {

    lateinit var navController: NavController

    override val mViewModel by activityViewModels<MainViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_setting

    override fun subscribeUi() {
        with(mViewModel) {

        }
        with(mDataBinding) {
//            vm = mViewModel
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}