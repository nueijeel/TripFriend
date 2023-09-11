package com.test.tripfriend.ui.myinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentMyAccompanyInfoBinding

class MyAccompanyInfoFragment : Fragment() {
    lateinit var fragmentMyAccompanyInfoBinding: FragmentMyAccompanyInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentMyAccompanyInfoBinding= FragmentMyAccompanyInfoBinding.inflate(layoutInflater)




        return fragmentMyAccompanyInfoBinding.root
    }


}