package com.test.tripfriend.ui.myinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.test.tripfriend.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentMyInfoMainBinding

class MyInfoMainFragment : Fragment() {
    lateinit var fragmentMyInfoMainBinding: FragmentMyInfoMainBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentMyInfoMainBinding= FragmentMyInfoMainBinding.inflate(layoutInflater)
        mainActivity=activity as MainActivity
        // Inflate the layout for this fragment

        fragmentMyInfoMainBinding.run {
            myInfoToolbar.run {
                setOnMenuItemClickListener {
                    //톱니 바퀴 클릭시 앱 설정 창으로 이동
                    true
                }
            }

            //자세히 보기 클릭 시
            buttonToDetailFriendSpeed.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.MY_ACCOMPANY_INFO_FRAGMENT,true,false,null)
            }

            seekbarFriendSpeed.setOnSeekBarChangeListener(object:OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    val padding=seekbarFriendSpeed.paddingLeft+seekbarFriendSpeed.paddingRight
                    val sPos=seekbarFriendSpeed.left+seekbarFriendSpeed.paddingLeft
                    val xPos=(seekbarFriendSpeed.width-padding)*seekbarFriendSpeed.progress/seekbarFriendSpeed.max+sPos-(textViewFriendSpeed.width/2)
                    textViewFriendSpeed.x= xPos.toFloat()
                    textViewFriendSpeed.text=seekbarFriendSpeed.progress.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })

        }


        return fragmentMyInfoMainBinding.root
    }


}