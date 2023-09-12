package com.test.tripfriend.ui.accompany

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.test.tripfriend.MainActivity
import com.test.tripfriend.databinding.FragmentAccompanyRegister2Binding

class AccompanyRegisterFragment2 : Fragment() {
    lateinit var fragmentAccompanyRegisterFragment2: FragmentAccompanyRegister2Binding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAccompanyRegisterFragment2 = FragmentAccompanyRegister2Binding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentAccompanyRegisterFragment2.run {
//            buttonRegister2Date.setOnClickListener {
//                val datePicker =
//                    MaterialDatePicker.Builder.datePicker()
//                        .setTitleText("Select date")
//                        .build()
//                datePicker.show()
//            }

            buttonAccompanyRegister2ToNextView.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.ACCOMPANY_REGISTER_FRAGMENT3, true, true, null)
            }
        }

        return fragmentAccompanyRegisterFragment2.root
    }
}