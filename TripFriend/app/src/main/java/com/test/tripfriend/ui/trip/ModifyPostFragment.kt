package com.test.tripfriend.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.tripfriend.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentModifyPostBinding

class ModifyPostFragment : Fragment() {
    lateinit var fragmentModifyPostBinding: FragmentModifyPostBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyPostBinding = FragmentModifyPostBinding.inflate(layoutInflater)

        fragmentModifyPostBinding.run {

        }

        return fragmentModifyPostBinding.root
    }
}
