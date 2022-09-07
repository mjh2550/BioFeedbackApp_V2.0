package com.android.tne.ui.activity

import androidx.activity.viewModels
import com.android.tne.R
import com.android.tne.base.BaseActivity
import com.android.tne.databinding.ActivityNaverLoginBinding
import com.android.tne.ui.viewmodel.LoginViewModel
//import com.navercorp.nid.NaverIdLoginSDK
//import com.navercorp.nid.oauth.NidOAuthLogin
//import com.navercorp.nid.oauth.OAuthLoginCallback
//import com.navercorp.nid.profile.NidProfileCallback
//import com.navercorp.nid.profile.data.NidProfileResponse

class NaverLoginActivity : BaseActivity<ActivityNaverLoginBinding, LoginViewModel>() {
/*    private val OAUTH_CLIENT_ID = getString(R.string.naver_oauth_client_id)
    private val OAUTH_CLIENT_SECRET = getString(R.string.naver_oauth_client_secret)
    private val OAUTH_CLIENT_NAME = getString(R.string.naver_oauth_client_name)*/

    override val mViewModel: LoginViewModel by viewModels()

    override fun getLayoutRes(): Int = R.layout.activity_naver_login

    override fun subscribeUi() {
        with(mViewModel) {

        }
        with(mDataBinding) {
            vm = mViewModel
          /*  btnNidOauthLogin.setOnClickListener {
                startNaverLogin()
            }
            btnLogout.setOnClickListener {
                startNaverLogout()
            }
            btnDelToken.setOnClickListener {
                startNaverDeleteToken()
            }*/
        }
    }
/*
    private fun startNaverLogin(){
        NaverIdLoginSDK.initialize(this,OAUTH_CLIENT_ID , OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                mDataBinding.tv01.text = "id: ${userId} \ntoken: ${naverToken}"

                Toast.makeText(this@NaverLoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@NaverLoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }


        *//** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. *//*
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@NaverLoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: $errorDescription", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }
    *//**
     * 로그아웃
     * 애플리케이션에서 로그아웃할 때는 다음과 같이 NaverIdLoginSDK.logout() 메서드를 호출합니다. *//*
    private fun startNaverLogout(){
        NaverIdLoginSDK.logout()
        Toast.makeText(this@NaverLoginActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }

    *//**
     * 연동해제
     * 네이버 아이디와 애플리케이션의 연동을 해제하는 기능은 다음과 같이 NidOAuthLogin().callDeleteTokenApi() 메서드로 구현합니다.
    연동을 해제하면 클라이언트에 저장된 토큰과 서버에 저장된 토큰이 모두 삭제됩니다.
     *//*
    private fun startNaverDeleteToken(){
        NidOAuthLogin().callDeleteTokenApi(this, object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                Toast.makeText(this@NaverLoginActivity, "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }*/

}