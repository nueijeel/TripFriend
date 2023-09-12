package com.test.tripfriend.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            textViewLoginMainJoin.setOnClickListener {
                loginMainActivity.replaceFragment(LoginMainActivity.JOIN_STEP_ONE_FRAGMENT, true, true, null)
            }
        }

        return fragmentLoginMainBinding.root
    }
}