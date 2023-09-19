package com.test.tripfriend.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.tripfriend.ui.main.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    lateinit var fragmentNotificationBinding:FragmentNotificationBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentNotificationBinding = FragmentNotificationBinding.inflate(layoutInflater)

        fragmentNotificationBinding.run {
            materialToolbarNotification.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.NOTIFICATION_FRAGMENT)
                }
            }
            tabLayoutNotification.run {}

            viewPager2Notification.run{
                adapter = FragmentPagerAdapter(mainActivity)

                TabLayoutMediator(tabLayoutNotification, this){ tab: TabLayout.Tab, i: Int ->
                    when(i){
                        0 -> tab.text = "받은 요청 내역"
                        1 -> tab.text = "보낸 요청 내역"
                    }
                }.attach()

            }

            //바텀 네비 가리기
            mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE
        }

        return fragmentNotificationBinding.root
    }

    // 뷰 페이저 어댑터
    class FragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(ReceivedNotificationFragment(), SentNotificationFragment())
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    //뒤로가기 했을 때 바텀네비 다시 보여주기
    override fun onPause() {
        super.onPause()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }
}