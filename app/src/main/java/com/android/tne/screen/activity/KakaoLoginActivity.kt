package com.android.tne.screen.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.android.tne.screen.viewmodel.LoginViewModel
import com.android.tne.base.BaseActivity
import com.android.tne.R
import com.android.tne.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(){
    override val mViewModel: LoginViewModel by viewModels()
    private lateinit var mAuth: FirebaseAuth
    private val mGso: GoogleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("926945233428-gvscbqf7panoe31s2o5k2i31b5ite3b2.apps.googleusercontent.com")     // R.String.형식으로 가져오면 왜 안되는지 모르겠음 toString()으로 변환해도 안됨
        .requestEmail()
        .build()

    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun subscribeUi() {
        mAuth = Firebase.auth
        with(mViewModel) {

        }
        with(mDataBinding) {
            vm = mViewModel
            mDataBinding.btnKakaoLogin.setOnClickListener {
                loginKakao()
            }
            mDataBinding.btnKakaoLogout.setOnClickListener {
                logoutKakao()
            }
            mDataBinding.btnKakaoDelete.setOnClickListener {
                deleteKakao()
            }
            mDataBinding.btnGoogleLogin.setOnClickListener {
                loginGoogle()
            }
            mDataBinding.btnGoogleLogout.setOnClickListener {
                logoutGoogle()
            }
            mDataBinding.btnGoogleDelete.setOnClickListener {
                deleteGoogle()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let { // 구글 자동 로그인 성공
            Toast.makeText(this, "구글 자동 로그인 성공", Toast.LENGTH_SHORT).show()
            Log.i("구글", "${it.email} 자동 로그인 성공")
        }
    }

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("카카오", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("카카오", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    private fun loginKakao(){
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("카카오", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기했을 시)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도(인터넷 창 생성)
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
                } else if (token != null) {
                    Log.i("카카오", "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
        }
    }

    private fun logoutKakao(){  // 토큰을 전부 만료시킴
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("카카오", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i("카카오", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

    private fun deleteKakao(){  // 토큰을 삭제 시킴 => 연결 끊
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("카카오", "연결 끊기 실패", error)
            }
            else {
                Log.i("카카오", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }

    // google Intent 결과
    private val getgoogleResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            registerGoogleUser(account.idToken!!)
        } catch (e:ApiException){
            Log.e("구글", "${e} 로그인 실패. 로그인 데이터 없음")
        }
    }

    private fun loginGoogle(){
        val mGoogleSignInClient = GoogleSignIn.getClient(this, mGso)
        val googleSignIntent: Intent = mGoogleSignInClient.signInIntent
        getgoogleResult.launch(googleSignIntent)
    }

    private fun registerGoogleUser(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                Log.i("구글", "구글 로그인 완료")
            }
    }

    private fun logoutGoogle(){
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, mGso)
        googleSignInClient.signOut().addOnSuccessListener {
            mAuth.signOut()
            Log.i("구글", "구글 로그아웃 완료")
        }
    }

    private fun deleteGoogle(){
        mAuth.currentUser?.let {
            it.delete()
            Log.i("구글", "사용자 데이터 삭제")
        }
    }
}