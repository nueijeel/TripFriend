package com.test.tripfriend

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.test.tripfriend.databinding.ActivityMainBinding
import com.test.tripfriend.ui.chatting.ChattingMainFragment
import com.test.tripfriend.ui.chatting.GroupChatRoomFragment
import com.test.tripfriend.ui.chatting.PersonalChatRoomFragment
import com.test.tripfriend.ui.home.HomeMainFragment
import com.test.tripfriend.ui.myinfo.MyAccompanyInfoFragment
import com.test.tripfriend.ui.myinfo.MyInfoMainFragment
import com.test.tripfriend.ui.trip.NotificationFragment
import com.test.tripfriend.ui.trip.TripMainFragment

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    var selectMenu = 0

    companion object {
        val HOME_MAIN_FRAGMENT = "HomeMainFragment"
        val TRIP_MAIN_FRAGMENT = "TripMainFragment"
        val CHATTING_MAIN_FRAGMENT = "ChattingMainFragment"
        val MYINFO_MAIN_FRAGMENT = "MyInfoMainFragment"
        val NOTIFICATION_FRAGMENT = "NotificationFragment"
        val MY_INFO_MAIN_FRAGMENT = "MyInfoMainFragment"
        val MY_ACCOMPANY_INFO_FRAGMENT="MyAccompanyInfoFragment"
        val PERSONAL_CHAT_ROOM_FRAGMENT = "PersonalChatRoomFragment"
        val GROUP_CHAT_ROOM_FRAGMENT = "GroupChatRoomFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // 시작화면 설정
        replaceFragment(HOME_MAIN_FRAGMENT, false, true, null)

        activityMainBinding.run {
            bottomNavigationViewMain.run {
                this.selectedItemId = R.id.navigationHome
                selectMenu = R.id.navigationHome

                setOnItemSelectedListener {
                    //선택된 메뉴를 다시 클릭할 때 선택을 넘기는 조건문
                    if (it.itemId == selectedItemId){
                        return@setOnItemSelectedListener false
                    }
                    when (it.itemId) {
                        //홈 클릭
                        R.id.navigationHome -> {
                            selectMenu = it.itemId
                            replaceFragment(HOME_MAIN_FRAGMENT, false, true, null)
                        }
                        //여행 클릭
                        R.id.navigationTrip -> {
                            selectMenu = it.itemId
                            replaceFragment(TRIP_MAIN_FRAGMENT,false,true,null)
                        }
                        //채팅 클릭
                        R.id.navigationChatting -> {
                            selectMenu = it.itemId
                            replaceFragment(CHATTING_MAIN_FRAGMENT, false, true, null)
                        }
                        //내정보 클릭
                        R.id.navigationMyInfo -> {
                            selectMenu = it.itemId
                            replaceFragment(MYINFO_MAIN_FRAGMENT, false, true, null)
                        }
                        else -> {
                            replaceFragment(HOME_MAIN_FRAGMENT, false, true, null)
                        }
                    }
                    true
                }
            }
        }

    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: String, addToBackStack: Boolean, animate: Boolean, bundle: Bundle?) {
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 새로운 Fragment를 담을 변수
        var newFragment = when (name) {
            HOME_MAIN_FRAGMENT -> HomeMainFragment()
            TRIP_MAIN_FRAGMENT -> TripMainFragment()
            CHATTING_MAIN_FRAGMENT -> ChattingMainFragment()
            MYINFO_MAIN_FRAGMENT -> MyInfoMainFragment()
            NOTIFICATION_FRAGMENT -> NotificationFragment()
            MY_INFO_MAIN_FRAGMENT -> MyInfoMainFragment()
            MY_ACCOMPANY_INFO_FRAGMENT->MyAccompanyInfoFragment()
            MYINFO_MAIN_FRAGMENT -> MyInfoMainFragment()
            PERSONAL_CHAT_ROOM_FRAGMENT -> PersonalChatRoomFragment()
            GROUP_CHAT_ROOM_FRAGMENT -> GroupChatRoomFragment()

            else -> Fragment()
        }

        newFragment.arguments = bundle

        if (newFragment != null) {

            // Fragment를 교체한다.
            fragmentTransaction.replace(R.id.fragmentContainerMain, newFragment)

            if (animate) {
                // 애니메이션을 설정한다.
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }

            if (addToBackStack) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }

            // 교체 명령이 동작하도록 한다.
            fragmentTransaction.commit()
        }
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name: String) {
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}