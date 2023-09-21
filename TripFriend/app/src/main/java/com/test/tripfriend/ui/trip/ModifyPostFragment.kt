package com.test.tripfriend.ui.trip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.tripfriend.ui.main.MainActivity
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

        // 이전 화면 정보 가져오기
        val tripPostDocumentId = arguments?.getString("postId")!!
        val tripPostWriterEmail = arguments?.getString("tripPostWriterEmail")

        fragmentModifyPostBinding.run {
            materialToolbarModifyPost.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODFY_POST_FRAGMENT)
                }
            }

            mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

            buttonModifyPostToNextView.setOnClickListener {
                // 임시 데이터
                val country = "ㅁㅇ"
                val locality = "ㅁㅇ"
                val latitude = 0.0
                val longitude = 0.0
                //
                // 다음 화면으로 정보 전달
                val newBundle = Bundle()
                newBundle.putString("tripPostDocumentId", tripPostDocumentId)
                newBundle.putString("tripPostWriterEmail", tripPostWriterEmail)
                newBundle.putString("country", country + " " + locality)
                newBundle.putDouble("latitude", latitude)
                newBundle.putDouble("longitude", longitude)

                mainActivity.replaceFragment(MainActivity.MODFY_POST2_FRAGMENT,true,true, newBundle)
            }
        }

        return fragmentModifyPostBinding.root
    }

    override fun onPause() {
        super.onPause()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }
}
