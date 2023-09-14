package com.test.tripfriend.ui.user

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentJoinStepOneBinding

class JoinStepOneFragment : Fragment() {

    lateinit var fragmentJoinStepOneBinding: FragmentJoinStepOneBinding
    lateinit var loginMainActivity: LoginMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentJoinStepOneBinding = FragmentJoinStepOneBinding.inflate(layoutInflater)
        loginMainActivity = activity as LoginMainActivity

        fragmentJoinStepOneBinding.run {

            //툴바 설정
            materialToolbarJoinStepOne.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationIconTint(Color.BLACK)
                setNavigationOnClickListener {
                    loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_ONE_FRAGMENT)
                }
            }

            //프로그래스바 설정
            progressBarJoinStepOne.run {
                //setStateNumberTypeface("nanumbarunpenregular")
            }
            var checkEmail = 0
            buttonJoinStepOneAuth.setOnClickListener {
                //입력 하지 않았을 경우
                if(textInputEditTextJoinStepOneEmail.text.toString() == "") {
                    val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                        setTitle("이메일 입력 오류")
                        setMessage("이메일을 입력해주세요.")
                        setNegativeButton("확인", null)
                    }
                    builder.show()
                }
                //입력 했을 때
                else{
                    val userEmail = textInputEditTextJoinStepOneEmail.text.toString()
                    if (isEmailValid(userEmail) == false) {
                        val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                            setTitle("이메일 입력 오류")
                            setMessage("유효하지 않은 이메일 주소입니다.")
                            setNegativeButton("확인", null)
                        }
                        builder.show()
                    } else {
                        //이메일 인증
                        checkEmail = 1
                        buttonJoinStepOneAuth.text = "인증완료"
                    }
                    //이메일 인증 후
                    //loginMainActivity.userEmail = textInputEditTextJoinStepOneEmail.text.toString()
                    //이메일 인증 후
                }
            }

            //버튼 클릭 화면 전환
            buttonJoinStepOneNext.setOnClickListener {
                if(checkEmail == 1){
                    if(checkBoxJoinStepOnePersonalInfoTerms.isChecked == false || checkBoxJoinStepOneServiceTerms.isChecked == false){
                        val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                            setTitle("약관 동의 필요")
                            setMessage("약관에 동의해주세요.")
                            setNegativeButton("확인", null)
                        }
                        builder.show()
                    }
                    else{
                        loginMainActivity.replaceFragment(LoginMainActivity.JOIN_STEP_TWO_FRAGMENT, true, true, null)
                    }
                }
                else{
                    val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                        setTitle("이메일 입력 오류")
                        setMessage("이메일 인증을 진행해주세요.")
                        setNegativeButton("확인", null)
                    }
                    builder.show()
                }

            }

            //이용 약관 내용 보기
            textViewJoinStepOneServiceTerms.setOnClickListener {
                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                    setTitle("Trip Friend 서비스 이용 약관")
                    setMessage(R.string.service_terms_contents)
                    setNegativeButton("확인", null)
                }
                builder.show()
            }

            //개인 정보 약관 내용 보기
            textViewJoinStepOnePersonalInfoTerms.setOnClickListener {
                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                    setTitle("개인 정보 수집 및 이용 동의")
                    setMessage(R.string.personal_info_terms_contents)
                    setNegativeButton("확인", null)
                }
                builder.show()
            }
        }

        return fragmentJoinStepOneBinding.root

    }
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
}