package com.test.tripfriend.ui.user

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentEmailLoginBinding
import com.test.tripfriend.repository.UserRepository

class EmailLoginFragment : Fragment() {

    lateinit var fragmentEmailLoginBinding: FragmentEmailLoginBinding
    lateinit var loginMainActivity: LoginMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentEmailLoginBinding = FragmentEmailLoginBinding.inflate(inflater)
        loginMainActivity = activity as LoginMainActivity

        fragmentEmailLoginBinding.run {

            //툴바 백버튼 설정
            materialToolbarEmailLogin.run {

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationIconTint(Color.BLACK)
                setNavigationOnClickListener {
                    loginMainActivity.removeFragment(LoginMainActivity.EMAIL_LOGIN_FRAGMENT)
                }
            }

            //로그인 버튼 클릭 이벤트
            buttonEmailLoginSubmit.setOnClickListener {
                var checkMail = 0
                var checkPw = 0
                UserRepository.getAllUser {
                    UserRepository.getAllUser() {
                        for (document in it.result.documents) {
                            if (textInputEditTextEmailLoginId.text.toString() == document.getString("userEmail")) {
                                checkMail = 1
                            }
                            if(textInputEditTextEmailLoginPw.text.toString() == document.getString("userPw")) {
                                checkPw = 1
                            }
                        }
                        if(checkMail == 1 && checkPw == 1){
                            val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                setTitle("로그인 성공")
                                setMessage("로그인에 성공하였습니다. 트립친과 함께 좋은 여행이 되길 바랍니다.^^")
                                setNegativeButton("메인화면으로"){ dialogInterface: DialogInterface, i: Int ->
                                    val intent = Intent(loginMainActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    loginMainActivity.finish()
                                }
                            }
                            val dialog = builder.create()
                            dialog.setOnDismissListener {
                                val intent = Intent(loginMainActivity, MainActivity::class.java)
                                startActivity(intent)
                                loginMainActivity.finish()
                            }
                            dialog.show()
                        }
                        else{
                            val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                setTitle("로그인 실패")
                                setMessage("이메일과 비밀번호를 확인해주세요.")
                                setNegativeButton("확인", null)
                            }
                            builder.show()
                        }
                    }
                }

            }
        }

        return fragmentEmailLoginBinding.root
    }
}