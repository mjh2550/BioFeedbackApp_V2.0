package com.android.tne.base

import android.app.Application
//import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
//        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}