package com.test.tripfriend.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.test.tripfriend.R
import com.test.tripfriend.databinding.ActivityLoginMainBinding
import kotlin.concurrent.thread

class LoginMainActivity : AppCompatActivity() {

    lateinit var activityLoginMainBinding: ActivityLoginMainBinding

    companion object {
        val LOGIN_MAIN_FRAGMENT = "LoginMainFragment"
        val EMAIL_LOGIN_FRAGMENT = "EmailLoginFragment"
        val JOIN_STEP_ONE_FRAGMENT = "JoinStepOneFragment"
        val JOIN_STEP_TWO_FRAGMENT = "JoinStepTwoFragment"
        val JOIN_STEP_THREE_FRAGMENT = "JoinStepThreeFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        activityLoginMainBinding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(activityLoginMainBinding.root)

        replaceFragment(LOGIN_MAIN_FRAGMENT, false, true, null)
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name:String, addToBackStack:Boolean, animate:Boolean, bundle:Bundle?){
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // 새로운 Fragment를 담을 변수
        var newFragment = when(name){
            LOGIN_MAIN_FRAGMENT -> LoginMainFragment()
            EMAIL_LOGIN_FRAGMENT -> EmailLoginFragment()
            JOIN_STEP_ONE_FRAGMENT -> JoinStepOneFragment()
            JOIN_STEP_TWO_FRAGMENT -> JoinStepTwoFragment()
            JOIN_STEP_THREE_FRAGMENT -> JoinStepThreeFragment()
            else -> Fragment()
        }

        newFragment.arguments = bundle

        if(newFragment != null) {
            // Fragment를 교채한다.
            fragmentTransaction.replace(R.id.fragmentContainerViewLoginMain, newFragment)

            if (animate == true) {
                // 애니메이션을 설정한다.
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }

            if (addToBackStack == true) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }

            // 교체 명령이 동작하도록 한다.
            fragmentTransaction.commit()
        }
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name:String){
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}