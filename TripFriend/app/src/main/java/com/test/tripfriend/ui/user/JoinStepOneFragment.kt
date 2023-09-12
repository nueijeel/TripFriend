package com.test.tripfriend.ui.user

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                setNavigationOnClickListener {
                    loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_ONE_FRAGMENT)
                }
            }

            //프로그래스바 설정
            progressBarJoinStepOne.run {
                //setStateNumberTypeface("nanumbarunpenregular")
            }

            //버튼 클릭 화면 전환
            buttonJoinStepOneNext.setOnClickListener {
                loginMainActivity.replaceFragment(LoginMainActivity.JOIN_STEP_TWO_FRAGMENT, true, true, null)
            }
        }

        return fragmentJoinStepOneBinding.root

    }
}