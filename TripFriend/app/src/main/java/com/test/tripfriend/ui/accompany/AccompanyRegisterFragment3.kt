package com.test.tripfriend.ui.accompany

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.test.tripfriend.MainActivity
import com.test.tripfriend.databinding.FragmentAccompanyRegister3Binding

class AccompanyRegisterFragment3 : Fragment() {
    lateinit var fragmentAccompanyRegisterFragment3: FragmentAccompanyRegister3Binding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAccompanyRegisterFragment3 = FragmentAccompanyRegister3Binding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentAccompanyRegisterFragment3.run {
            materialToolbarRegister3.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.ACCOMPANY_REGISTER_FRAGMENT3)
                }
            }

            buttonAccompanyRegister3ToSubmit.setOnClickListener {
                Snackbar.make(mainActivity.activityMainBinding.root, "등록이 완료되었습니다..", Snackbar.LENGTH_SHORT).show()

                mainActivity.removeFragment(MainActivity.ACCOMPANY_REGISTER_FRAGMENT3)
                mainActivity.removeFragment(MainActivity.ACCOMPANY_REGISTER_FRAGMENT2)
                mainActivity.removeFragment(MainActivity.ACCOMPANY_REGISTER_FRAGMENT1)

                mainActivity.replaceFragment(MainActivity.READ_POST_FRAGMENT, false, true, null)

            }
        }

        return fragmentAccompanyRegisterFragment3.root
    }
}