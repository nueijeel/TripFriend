package com.test.tripfriend.ui.myinfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.tripfriend.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentMyAppSettingBinding

class MyAppSettingFragment : Fragment() {
    lateinit var fragmentMyAppSettingBinding:FragmentMyAppSettingBinding
    lateinit var mainActivity:MainActivity
    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentMyAppSettingBinding= FragmentMyAppSettingBinding.inflate(layoutInflater)
        mainActivity=activity as MainActivity

        fragmentMyAppSettingBinding.run {

            appSettingToolbar.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                //툴바 뒤로가기
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MY_APP_SETTING_FRAGMENT)
                    mainActivity.activityMainBinding.bottomNavigationViewMain.visibility=View.VISIBLE
                }
            }

            //계정 정보 수정 버튼
            buttonChangeMyInfo.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.MODIFY_MY_INFO_FRAGMENT,true,false,null)
            }

            //로그아웃 버튼
            buttonLogOut.setOnClickListener {
                val builder=MaterialAlertDialogBuilder(mainActivity,R.style.DialogTheme).apply {
                    setTitle("로그아웃")
                    setMessage("현재 로그인 된 계정에서 로그아웃 됩니다")
                    setNegativeButton("취소", null)
                    setPositiveButton("로그아웃", null)
                }
                builder.show()
            }

            //회원탈퇴 버튼
            buttonSignOut.setOnClickListener {
                MaterialAlertDialogBuilder(mainActivity,R.style.DialogTheme).run {
                    setTitle("회원탈퇴")
                    setMessage("회원탈퇴를 하시면 저장된 모든 정보가 삭제되며 삭제된 정보는 복구할 수 없습니다.")
                    setNegativeButton("취소", null)
                    setPositiveButton("회원탈퇴", null)
                    show()
                }
            }

        }



        return fragmentMyAppSettingBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val main = activity as MainActivity

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                main.removeFragment(MainActivity.MY_APP_SETTING_FRAGMENT)
                main.activityMainBinding.bottomNavigationViewMain.visibility=View.VISIBLE

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}