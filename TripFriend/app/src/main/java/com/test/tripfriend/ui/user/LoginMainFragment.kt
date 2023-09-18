package com.test.tripfriend.ui.user

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentLoginMainBinding
import com.test.tripfriend.repository.UserRepository

class LoginMainFragment : Fragment() {

    lateinit var fragmentLoginMainBinding: FragmentLoginMainBinding
    lateinit var loginMainActivity: LoginMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginMainActivity = activity as LoginMainActivity
        fragmentLoginMainBinding = FragmentLoginMainBinding.inflate(inflater)

        fragmentLoginMainBinding.run {
            buttonLoginMainEmailLogin.setOnClickListener {
                loginMainActivity.replaceFragment(LoginMainActivity.EMAIL_LOGIN_FRAGMENT, true, true, null)
            }
            //카카오 로그인
            buttonLoginMainKakaoLogin.run {
                setOnClickListener {
                    KakaoSdk.init(loginMainActivity, "381e6f58b057f226b499d671682fe581")
                    kakaoLogin() //로그인
                }
            }
            buttonLoginMainNaverLogin.run {
                setOnClickListener {
                    kakaoLogout()
                    kakaoUnlink()
                }
            }

            textViewLoginMainJoin.setOnClickListener {
                loginMainActivity.replaceFragment(LoginMainActivity.JOIN_STEP_ONE_FRAGMENT, true, true, null)
            }

            textViewLoginMainNonMember.setOnClickListener {
                val intent = Intent(loginMainActivity, MainActivity::class.java)
                startActivity(intent)

                loginMainActivity.finish()
            }
        }

        return fragmentLoginMainBinding.root
    }
    fun kakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
            } else if (token != null) {
                //최종적으로 카카오로그인 및 유저정보 가져온 결과
                UserApiClient.instance.me { user, error ->
                    loginMainActivity.userAuth = "카카오"
                    loginMainActivity.userEmail = "${user?.kakaoAccount?.email}"
                    loginMainActivity.userNickname = user?.properties?.get("nickname").toString()
                    var checkAuth = 1
                    var checkLogin = 1
                    //이메일 중복 검사
                    UserRepository.getAllUser{
                        for (document in it.result.documents) {
                            //중복된 이메일은 있지만 인증 방식이 카카오가 아니면
                            if (loginMainActivity.userEmail == document.getString("userEmail")
                                && loginMainActivity.userAuth != document.getString("userAuthentication")) {
                                checkAuth = 0
                                val builder = MaterialAlertDialogBuilder(loginMainActivity, R.style.DialogTheme).apply {
                                    setTitle("이메일 입력 오류")
                                    setMessage("현재 사용중인 이메일입니다.")
                                    setNegativeButton("확인", null)
                                }
                                builder.show()
                                break
                            }
                            //중복된 이메일이 있고 인증 방식이 카카오면
                            else if(loginMainActivity.userEmail == document.getString("userEmail")
                                && loginMainActivity.userAuth == document.getString("userAuthentication")){
                                checkLogin = 0
                                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                    setTitle("로그인 성공")
                                    setMessage("로그인에 성공하였습니다. 트립친과 함께 좋은 여행이 되길 바랍니다.^^")
                                    setNegativeButton("메인화면으로",null)
                                    setOnDismissListener{
                                        val intent = Intent(loginMainActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        loginMainActivity.finish()
                                    }
                                }
                                builder.show()
                            }
                        }
                        //중복된 이메일도 없고 첫 로그인일 때
                        if(checkAuth == 1 && checkLogin == 1){
                            //카카오 정보 기반으로 회원가입 진행
                            loginMainActivity.replaceFragment(LoginMainActivity.MORE_KAKAO_INFO_INPUT_FRAGMENT,true,true,null)
                        }
                    }

                    Log.d("aaaa","===============================================")
                    Log.d("aaaa","이메일 = ${loginMainActivity.userEmail}")
                    Log.d("aaaa","비밀번호 = ${loginMainActivity.userPw}")
                    Log.d("aaaa","인증방식 = ${loginMainActivity.userAuth}")
                    Log.d("aaaa","이름 = ${loginMainActivity.userName}")
                    Log.d("aaaa","닉네임 = ${loginMainActivity.userNickname}")
                    Log.d("aaaa","휴대폰 번호 = ${loginMainActivity.userPhoneNumber}")
                    Log.d("aaaa","MBTI = ${loginMainActivity.userMBTI}")
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(loginMainActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(loginMainActivity) { token, error ->
                if (error != null) {
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(loginMainActivity, callback = callback)
                } else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        loginMainActivity.userAuth = "카카오"
                        loginMainActivity.userEmail = "${user?.kakaoAccount?.email}"
                        loginMainActivity.userNickname = user?.properties?.get("nickname").toString()
                        var checkAuth = 1
                        var checkLogin = 1
                        //이메일 중복 검사
                        UserRepository.getAllUser{
                            for (document in it.result.documents) {
                                //중복된 이메일은 있지만 인증 방식이 카카오가 아니면
                                if (loginMainActivity.userEmail == document.getString("userEmail")
                                    && loginMainActivity.userAuth != document.getString("userAuthentication")) {
                                    checkAuth = 0
                                    val builder = MaterialAlertDialogBuilder(loginMainActivity, R.style.DialogTheme).apply {
                                        setTitle("이메일 입력 오류")
                                        setMessage("현재 사용중인 이메일입니다.")
                                        setNegativeButton("확인", null)
                                    }
                                    builder.show()
                                    break
                                }
                                //중복된 이메일이 있고 인증 방식이 카카오면
                                else if(loginMainActivity.userEmail == document.getString("userEmail")
                                    && loginMainActivity.userAuth == document.getString("userAuthentication")){
                                    val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                        setTitle("로그인 성공")
                                        setMessage("로그인에 성공하였습니다. 트립친과 함께 좋은 여행이 되길 바랍니다.^^")
                                        setNegativeButton("메인화면으로",null)
                                        setOnDismissListener{
                                            val intent = Intent(loginMainActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            loginMainActivity.finish()
                                        }
                                    }
                                    builder.show()
                                }
                            }
                            //중복된 이메일도 없고 첫 로그인일 때
                            if(checkAuth == 1 && checkLogin == 1){
                                //카카오 정보 기반으로 회원가입 진행
                                loginMainActivity.replaceFragment(LoginMainActivity.MORE_KAKAO_INFO_INPUT_FRAGMENT,true,true,null)
                            }
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(loginMainActivity, callback = callback)
        }
    }
    fun TextMsg(act: Activity, msg : String){
        fragmentLoginMainBinding.buttonLoginMainKakaoLogin.setText(msg)
    }

    fun kakaoLogout(){
        UserApiClient.instance.logout { error ->
            if (error != null) {

            }
            else {

            }
        }
    }
    fun kakaoUnlink(){
        UserApiClient.instance.unlink { error ->
            if (error != null) {

            }
            else {

            }
        }
    }
}