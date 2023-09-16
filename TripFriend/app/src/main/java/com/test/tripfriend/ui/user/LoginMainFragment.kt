package com.test.tripfriend.ui.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentLoginMainBinding

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
            buttonLoginMainKakaoLogin.run {
                setOnClickListener {
                    KakaoSdk.init(loginMainActivity, "381e6f58b057f226b499d671682fe581")
                    kakaoLogin() //로그인
                }
            }
            buttonLoginMainNaverLogin.run {
                setOnClickListener {
                    kakaoLogout()
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
                TextMsg(loginMainActivity, "카카오계정으로 로그인 실패 : ${error}")
            } else if (token != null) {
                //최종적으로 카카오로그인 및 유저정보 가져온 결과
                UserApiClient.instance.me { user, error ->
                    TextMsg(loginMainActivity, "카카오계정으로 로그인 성공 \n\n " +
                            "token: ${token.accessToken} \n\n " +
                            "me: ${user}")

                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(loginMainActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(loginMainActivity) { token, error ->
                if (error != null) {
                    TextMsg(loginMainActivity, "카카오톡으로 로그인 실패 : ${error}")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(loginMainActivity, callback = callback)
                } else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        TextMsg(loginMainActivity, "카카오계정으로 로그인 성공 \n\n " +
                                "닉네임: ${user?.properties?.get("nickname").toString()}\n"+
                                "이메일: ${user?.kakaoAccount?.email}\n")
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
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                TextMsg(loginMainActivity, "로그아웃 실패. SDK에서 토큰 삭제됨: ${error}")
            }
            else {
                TextMsg(loginMainActivity, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
}