package com.test.tripfriend.ui.user

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentJoinStepThreeBinding
import kotlin.math.log

class JoinStepThreeFragment : Fragment() {

    lateinit var fragmentJoinStepThreeBinding: FragmentJoinStepThreeBinding
    lateinit var loginMainActivity: LoginMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentJoinStepThreeBinding = FragmentJoinStepThreeBinding.inflate(inflater)
        loginMainActivity = activity as LoginMainActivity

        fragmentJoinStepThreeBinding.run {
            materialToolbarJoinStepThree.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationIconTint(Color.BLACK)
                setNavigationOnClickListener {
                    loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_THREE_FRAGMENT)
                }
            }

            progressBarJoinStepThree.run {
                setOnStateItemClickListener { stateProgressBar, stateItem, stateNumber, isCurrentState ->
                    when(stateNumber){
                        1-> {
                            loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_THREE_FRAGMENT)
                            loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_TWO_FRAGMENT)
                        }
                        2-> { loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_THREE_FRAGMENT) }
                    }
                }
            }
            buttonJoinStepThreeNickNameCheck.setOnClickListener {
                //닉네임 중복 검사 해야함
                textViewJoinStepThreeNickNameComplete.visibility = View.VISIBLE
            }

            buttonJoinStepThreeNext.setOnClickListener {
                loginMainActivity.userName = textInputEditTextJoinStepThreeName.text.toString()
                loginMainActivity.userNickname = textInputEditTextJoinStepThreeNickName.text.toString()
                Log.d("aaaa","===============================================")
                Log.d("aaaa","이메일 = ${loginMainActivity.userEmail}")
                Log.d("aaaa","비밀번호 = ${loginMainActivity.userPw}")
                Log.d("aaaa","인증방식 = ${loginMainActivity.userAuth}")
                Log.d("aaaa","이름 = ${loginMainActivity.userName}")
                Log.d("aaaa","닉네임 = ${loginMainActivity.userNickname}")
                Log.d("aaaa","휴대폰 번호 = ${loginMainActivity.userPhoneNumber}")
                Log.d("aaaa","MBTI = ${loginMainActivity.userMBTI}")
                loginMainActivity.replaceFragment(LoginMainActivity.JOIN_STEP_FOUR_FRAGMENT, true, true, null)
            }
        }

        return fragmentJoinStepThreeBinding.root
    }
}