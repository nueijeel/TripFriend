package com.test.tripfriend.ui.trip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentTripMainBinding

class TripMainFragment : Fragment() {
    lateinit var fragmentTripMainBinding: FragmentTripMainBinding
    lateinit var mainActivity: MainActivity
    var position = 0
    lateinit var viewPager: ViewPager2
    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTripMainBinding = FragmentTripMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        viewPager = fragmentTripMainBinding.viewPager2TripMain
        viewPagerAdapter = ViewPagerAdapter(mainActivity)
        viewPager.adapter = viewPagerAdapter

        fragmentTripMainBinding.run {
            materialToolbarTripMain.run {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_item_notification -> {

                            val tabLayout = fragmentTripMainBinding.tabLayoutTripMain
                            position = tabLayout.selectedTabPosition
                            when(tabLayout.selectedTabPosition){
                                0 ->{
                                    val bundle = Bundle()
                                    bundle.putInt("position",0)
                                    mainActivity.replaceFragment(
                                        MainActivity.NOTIFICATION_FRAGMENT, true, true, bundle)
                                }
                                1 ->{
                                    val bundle = Bundle()
                                    bundle.putInt("position",1)
                                    mainActivity.replaceFragment(
                                        MainActivity.NOTIFICATION_FRAGMENT, true, true, bundle)
                                }
                                2 ->{
                                    val bundle = Bundle()
                                    bundle.putInt("position",2)
                                    mainActivity.replaceFragment(
                                        MainActivity.NOTIFICATION_FRAGMENT, true, true, bundle)
                                }
                            }

                            val bundle = Bundle()
                            bundle.putInt("fragment",tabLayout.selectedTabPosition)
                        }
                    }
                    true
                }
            }

            // 뷰페이저
            viewPager2TripMain.run {
                viewPager2TripMain.adapter = adapter

                TabLayoutMediator(tabLayoutTripMain, viewPager2TripMain) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = "참여중인 동행"
                        }
                        1 -> tab.text = "지난 동행"
                        2 -> tab.text = "찜목록"
                    }
                }.attach()
                // 탭의 텍스트 색상 변경
                val tabLayout = fragmentTripMainBinding.tabLayoutTripMain
                val selectedColor = ContextCompat.getColor(requireContext(), R.color.highLightColor)
                val unselectedColor = ContextCompat.getColor(requireContext(), R.color.gray)

                // 선택되지 않은 탭의 텍스트 색상 설정
                tabLayout.setTabTextColors(unselectedColor, selectedColor)
                // 선택된 탭의 텍스트 색상 설정
                tabLayout.setSelectedTabIndicatorColor(selectedColor)

            }
        }

        return fragmentTripMainBinding.root
    }

    // 뷰 페이저 어댑터
    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        val fragments: List<Fragment> = listOf(InProgressFragment(), PassFragment(), LikedListFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onResume() {
        super.onResume()
        viewPagerAdapter = ViewPagerAdapter(mainActivity)
        viewPager.adapter = viewPagerAdapter
        viewPager.setCurrentItem(mainActivity.tripMainPosition, false)
    }
}