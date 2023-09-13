package com.test.tripfriend.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.tripfriend.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentModifyPost2Binding

class ModifyPost2Fragment : Fragment() {
    lateinit var fragmentModifyPost2Binding: FragmentModifyPost2Binding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyPost2Binding = FragmentModifyPost2Binding.inflate(layoutInflater)

        fragmentModifyPost2Binding.run {
            materialToolbarModifyPost2.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODFY_POST2_FRAGMENT)
                }
            }

            mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE
            buttonAccompanyModifyPost2ToNextView.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.MODFY_POST3_FRAGMENT,true,true,null)
            }
        }

        return fragmentModifyPost2Binding.root
    }

    override fun onPause() {
        super.onPause()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }
}