package com.android.tne.ui.activity

import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.tne.base.BaseActivity
import com.android.tne.R
import com.android.tne.databinding.ActivityMainBinding
import com.android.tne.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity  @Inject constructor(
): BaseActivity<ActivityMainBinding, MainViewModel>() {

    lateinit var navHostFragment : NavHostFragment
    lateinit var navController: NavController

    override val mViewModel: MainViewModel by viewModels()

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun subscribeUi() {
        with(mViewModel) {
 /*           mViewModel.testStr.observe(this@MainActivity){
                Log.d("observe","ata")
            }*/
            boardList.observe(this@MainActivity){
                Log.d("observe : ","called")
            }
        }
        with(mDataBinding) {
            vm = mViewModel
        }
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        mDataBinding.bottomNavigationView.setupWithNavController(navController)
    }
}