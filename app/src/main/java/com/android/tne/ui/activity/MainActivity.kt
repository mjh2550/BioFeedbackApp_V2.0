package com.android.tne.ui.activity

import android.content.DialogInterface
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity  @Inject constructor(
): BaseActivity<ActivityMainBinding, MainViewModel>() {

    var backKeyPressedTime : Long = 0

    lateinit var navHostFragment : NavHostFragment
    lateinit var navController: NavController

    override val mViewModel: MainViewModel by viewModels()

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun subscribeUi() {
        with(mViewModel) {
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

    /**
     * 뒤로가기 키
     */
    override fun onBackPressed() {
//        super.onBackPressed()

        /**
         * 메인화면에서 뒤로가기 빠르게 두번 누를 시 종료 팝업 출력
         */
        if(System.currentTimeMillis() > backKeyPressedTime + 2500){
            backKeyPressedTime = System.currentTimeMillis()
            return
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2500){
            val alertDialog = AlertDialog.Builder(this@MainActivity)
                .setTitle("알림")
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("종료") { _, _ ->
                    finishAffinity()
                    exitProcess(0)
                }
                .setNegativeButton("취소") { _, _ -> }
            alertDialog.create().show()
        }
    }


}