package com.android.tne.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.tne.R
import com.android.tne.adapters.ViewPagerAdapter
import com.android.tne.base.BaseFragment
import com.android.tne.ble.ui.BLEMainActivity
import com.android.tne.databinding.FragmentHomeBinding
import com.android.tne.ui.activity.FcmActivity
import com.android.tne.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class HomeFragment() : BaseFragment<FragmentHomeBinding, MainViewModel>() , View.OnClickListener{
    lateinit var navController: NavController

    override val mViewModel by activityViewModels<MainViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun subscribeUi() {
        with(mViewModel) {

        }
        with(mDataBinding) {
            vm = mViewModel
            btnGoMeasure.setOnClickListener(this@HomeFragment)
            btnGoResult.setOnClickListener(this@HomeFragment)
            val testArray =
            viewPager.apply {
                clipToOutline = false
                clipChildren = false
                offscreenPageLimit = 1
                adapter = ViewPagerAdapter(getItems())
            }

            }

        }

    // 뷰 페이저에 들어갈 아이템
    private fun getItems(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.ride , R.drawable.running, R.drawable.ride
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_go_measure -> {
//                navController.navigate(R.id.action_bioStartFragment_to_bioStart1Fragment)
                //fragment는 activity 위에서 돌아가기 때문에 activity를 넣어줘야 함
                val intent = Intent(requireActivity(), FcmActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_go_result -> {
                //임시로 푸시메시지 보내는 창
                val intent = Intent(requireActivity() , BLEMainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}