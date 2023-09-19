package com.test.tripfriend.ui.trip

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentTripMainBinding
import com.test.tripfriend.databinding.RowTripMainBinding
import com.test.tripfriend.dataclassmodel.TripPost
import com.test.tripfriend.ui.home.HomeListFragment
import com.test.tripfriend.ui.home.HomeMainFragment
import com.test.tripfriend.ui.home.HomeMapFragment
import com.test.tripfriend.viewmodel.TripPostViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TripMainFragment : Fragment() {
    lateinit var fragmentTripMainBinding: FragmentTripMainBinding
    lateinit var mainActivity: MainActivity

    lateinit var tripPostViewModel: TripPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTripMainBinding = FragmentTripMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // initTripViewModel()

        fragmentTripMainBinding.run {
            materialToolbarTripMain.run {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_item_notification -> {
                            mainActivity.replaceFragment(
                                MainActivity.NOTIFICATION_FRAGMENT, true, true, null)
                        }
                    }
                    true
                }
            }

            // 뷰페이저
            viewPager2TripMain.run {
                val adapter = ViewPagerAdapter(mainActivity)
                viewPager2TripMain.adapter = adapter

                TabLayoutMediator(tabLayoutTripMain, viewPager2TripMain) { tab, position ->
                    when (position) {
                        0 -> tab.text = "참여중인 동행"
                        1 -> tab.text = "지난 동행"
                        2 -> tab.text = "찜목록"
                    }
                }.attach()
            }

            //상단 탭
            tabLayoutTripMain.run {
//                //다른 화면 갔다가 왔을 때 선택한 탭 유지시킴
//                val tabLayout = findViewById<TabLayout>(R.id.tabLayoutTripMain)
//                tabLayout.getTabAt(mainActivity.selectedTabPosition)?.select()
//
//                //탭 선택 했을 때 밑에 줄 색상 강조색으로 변경
//                setSelectedTabIndicatorColor(getResources().getColor(R.color.highLightColor))
//                //탭 선택 시 선택 안된 것들의 글자 색은 회색, 선택된 것의 글자색은 강조색
//                setTabTextColors(Color.GRAY, getResources().getColor(R.color.highLightColor))
//
//                //선택되어있는 탭 포지션따라 분기
//                when (mainActivity.selectedTabPosition) {
//                    0 -> {
//                        recyclerViewTripMain.run {
//                            adapter = TripMainAdapter()
//                            layoutManager = LinearLayoutManager(mainActivity)
//                        }
//                        textViewTripMainNoPost.visibility = View.GONE
//                    }
//
//                    1 -> {
//                        recyclerViewTripMain.run {
//                            adapter = TripMainAdapter()
//                            layoutManager = LinearLayoutManager(mainActivity)
//                        }
//                        textViewTripMainNoPost.visibility = View.GONE
////                        textViewTripMainNoPost.text = "지난 동행글 없음"
//                    }
//
//                    2 -> {
//                        recyclerViewTripMain.run {
//                            adapter = TripMainAdapter()
//                            layoutManager = LinearLayoutManager(mainActivity)
//                        }
//                        textViewTripMainNoPost.visibility = View.GONE
////                        textViewTripMainNoPost.text = "찜목록 없음"
//                    }
//                }
//
//                //탭을 직접 선택했을 때
//                tabLayoutTripMain.addOnTabSelectedListener(object :
//                    TabLayout.OnTabSelectedListener {
//                    override fun onTabSelected(tab: TabLayout.Tab?) {
//                        var selectedTabPosition = tab?.position!!
//                        when (selectedTabPosition) {
//                            0 -> {
//                                mainActivity.selectedTabPosition = 0
//                                recyclerViewTripMain.visibility = View.VISIBLE
//                                recyclerViewTripMain.run {
//                                    adapter = TripMainAdapter()
//                                    layoutManager = LinearLayoutManager(mainActivity)
//
//                                    //fragmentTripMainBinding.recyclerViewTripMain.adapter?.notifyDataSetChanged()
//                                }
//                                textViewTripMainNoPost.visibility = View.GONE
//
//                                Log.d("ㅁㅇselectedTabPosition", mainActivity.selectedTabPosition.toString())
//                            }
//
//                            1 -> {
//                                mainActivity.selectedTabPosition = 1
//                                recyclerViewTripMain.visibility = View.VISIBLE
//                                textViewTripMainNoPost.visibility = View.GONE
//                                recyclerViewTripMain.run {
//                                    adapter = TripMainAdapter()
//                                    layoutManager = LinearLayoutManager(mainActivity)
//
//                                    //fragmentTripMainBinding.recyclerViewTripMain.adapter?.notifyDataSetChanged()
//                                }
////                                textViewTripMainNoPost.text = "지난 동행글 없음"
//
//                                Log.d("ㅁㅇselectedTabPosition", mainActivity.selectedTabPosition.toString())
//                            }
//
//                            2 -> {
//                                mainActivity.selectedTabPosition = 2
//                                recyclerViewTripMain.visibility = View.VISIBLE
//                                textViewTripMainNoPost.visibility = View.GONE
//                                recyclerViewTripMain.run {
//                                    adapter = TripMainAdapter()
//                                    layoutManager = LinearLayoutManager(mainActivity)
//
//                                    //fragmentTripMainBinding.recyclerViewTripMain.adapter?.notifyDataSetChanged()
//                                }
////                                textViewTripMainNoPost.text = "찜목록 없음"
//
//                                Log.d("ㅁㅇselectedTabPosition", mainActivity.selectedTabPosition.toString())
//                            }
//                        }
//                    }
//
//                    override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//                    }
//
//                    override fun onTabReselected(tab: TabLayout.Tab?) {
//
//                    }
//                })
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

//    inner class TripMainAdapter : RecyclerView.Adapter<TripMainAdapter.TripMainViewHolder>() {
//        var tripPostItemList :List<TripPost> = emptyList()
//
//        fun updateItemList(newList: List<TripPost>) {
//            this.tripPostItemList = newList
//            notifyDataSetChanged()
//        }
//
//        inner class TripMainViewHolder(rowTripMainBinding: RowTripMainBinding) : RecyclerView.ViewHolder(rowTripMainBinding.root) {
//            val textViewTripMainRowTitle: TextView // 제목
//            val textViewNotificationDate: TextView // 날짜
//            val textViewTripMainRowNOP: TextView // 인원
//            val textViewTripMainRowLocation: TextView // 지역
//            val chipTripMainRowCategory1: Chip // 카테고리1
//            val chipTripMainRowCategory2: Chip // 카테고리2
//            val chipTripMainRowCategory3: Chip // 카테고리3
//            val textViewTripMainRowHashTag: TextView //해시태그
//
//            init {
//                textViewTripMainRowTitle = rowTripMainBinding.textViewTripMainRowTitle
//                textViewNotificationDate = rowTripMainBinding.textViewNotificationDate
//                textViewTripMainRowNOP = rowTripMainBinding.textViewTripMainRowNOP
//                textViewTripMainRowLocation = rowTripMainBinding.textViewTripMainRowLocation
//                chipTripMainRowCategory1 = rowTripMainBinding.chipTripMainRowCategory1
//                chipTripMainRowCategory2 = rowTripMainBinding.chipTripMainRowCategory2
//                chipTripMainRowCategory3 = rowTripMainBinding.chipTripMainRowCategory3
//                textViewTripMainRowHashTag = rowTripMainBinding.textViewTripMainRowHashTag
//
//                rowTripMainBinding.root.setOnClickListener {
//                    val newBundle = Bundle()
//                    newBundle.putString("tripPostWriterEmail", tripPostItemList[adapterPosition].tripPostWriterEmail)
//                    newBundle.putInt("tripPostIdx", tripPostItemList[adapterPosition].tripPostIdx)
//
//                    mainActivity.replaceFragment(MainActivity.READ_POST_FRAGMENT,true,true, newBundle)
//                }
//            }
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripMainViewHolder {
//            val rowTripMainBinding = RowTripMainBinding.inflate(layoutInflater)
//
//            rowTripMainBinding.root.run{
//                layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//            }
//
//            return TripMainViewHolder(rowTripMainBinding)
//        }
//
//        override fun getItemCount(): Int {
//            return tripPostItemList.size
//        }
//
//        override fun onBindViewHolder(holder: TripMainViewHolder, position: Int) {
//            // 오늘 날짜
//            val dateFormat = SimpleDateFormat("yyyyMMdd")
//
//            val today = Calendar.getInstance().apply {
//                set(Calendar.HOUR_OF_DAY, 0)
//                set(Calendar.MINUTE, 0)
//                set(Calendar.SECOND, 0)
//                set(Calendar.MILLISECOND, 0)
//            }.time.time
//
//            val selectDate = dateFormat.parse(tripPostItemList[position].tripPostDate!![1]).time
//
//            var dateBoolean = false
//
//            if((today - selectDate) / (24 * 60 * 60 * 1000) < 0) {
//                // 참여중인 동행
//                dateBoolean = false
//            } else {
//                // 지난 동행
//                dateBoolean = true
//            }
//
//            Log.d("ㅁㅇdateBoolean", dateBoolean.toString())
//            Log.d("ㅁㅇselectedTabPosition", mainActivity.selectedTabPosition.toString())
//            //선택된 탭이 "참여중인 동행"일 경우
//            if (mainActivity.selectedTabPosition == 0) {
//                if(!dateBoolean) {
//                    Log.d("ㅁㅇ", "참여중인 동행")
//                    holder.textViewTripMainRowTitle.text = tripPostItemList[position].tripPostTitle
//                    if(tripPostItemList[position].tripPostDate!![1] == null) {
//                        holder.textViewNotificationDate.text = tripPostItemList[position].tripPostDate!![0]
//                    } else {
//                        holder.textViewNotificationDate.text = "${tripPostItemList[position].tripPostDate!![0]} ~ ${tripPostItemList[position].tripPostDate!![1]}"
//                    }
//                    holder.textViewTripMainRowNOP.text = tripPostItemList[position].tripPostMemberCount.toString()
//                    holder.textViewTripMainRowLocation.text = tripPostItemList[position].tripPostLocationName
//                    when(tripPostItemList[position].tripPostTripCategory!!.size) {
//                        1 -> {
//                            holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                            holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                            holder.chipTripMainRowCategory2.visibility = View.INVISIBLE
//                            holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
//                        }
//                        2 -> {
//                            holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                            holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                            holder.chipTripMainRowCategory2.text = tripPostItemList[position].tripPostTripCategory!![1]
//                            holder.chipTripMainRowCategory2.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![1])
//                            holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
//                        }
//                        3 -> {
//                            holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                            holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                            holder.chipTripMainRowCategory2.text = tripPostItemList[position].tripPostTripCategory!![1]
//                            holder.chipTripMainRowCategory2.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![1])
//                            holder.chipTripMainRowCategory3.text = tripPostItemList[position].tripPostTripCategory!![2]
//                            holder.chipTripMainRowCategory3.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![2])
//                        }
//                        else -> {
//                            Snackbar.make(fragmentTripMainBinding.root, "Chip 오류 발생", Snackbar.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    holder.textViewTripMainRowHashTag.visibility = View.VISIBLE
//                    holder.textViewTripMainRowHashTag.text = tripPostItemList[position].tripPostHashTag
//                }
//            }
//            //선택된 탭이 "지난 동행"일 경우
//            else if (mainActivity.selectedTabPosition == 1) {
//                Log.d("ㅁㅇselectedTabPosition", mainActivity.selectedTabPosition.toString())
//                if(dateBoolean) {
//                    Log.d("ㅁㅇ", "지난동행")
//                    holder.textViewTripMainRowTitle.text = tripPostItemList[position].tripPostTitle
//                    if(tripPostItemList[position].tripPostDate!![1] == null) {
//                        holder.textViewNotificationDate.text = tripPostItemList[position].tripPostDate!![0]
//                    } else {
//                        holder.textViewNotificationDate.text = "${tripPostItemList[position].tripPostDate!![0]} ~ ${tripPostItemList[position].tripPostDate!![1]}"
//                    }
//                    holder.textViewTripMainRowNOP.text = tripPostItemList[position].tripPostMemberCount.toString()
//                    holder.textViewTripMainRowLocation.text = tripPostItemList[position].tripPostLocationName
//                    when(tripPostItemList[position].tripPostTripCategory!!.size) {
//                        1 -> {
//                            holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                            holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                            holder.chipTripMainRowCategory2.visibility = View.INVISIBLE
//                            holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
//                        }
//                        2 -> {
//                            holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                            holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                            holder.chipTripMainRowCategory2.text = tripPostItemList[position].tripPostTripCategory!![1]
//                            holder.chipTripMainRowCategory2.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![1])
//                            holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
//                        }
//                        3 -> {
//                            holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                            holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                            holder.chipTripMainRowCategory2.text = tripPostItemList[position].tripPostTripCategory!![1]
//                            holder.chipTripMainRowCategory2.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![1])
//                            holder.chipTripMainRowCategory3.text = tripPostItemList[position].tripPostTripCategory!![2]
//                            holder.chipTripMainRowCategory3.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![2])
//                        }
//                        else -> {
//                            Snackbar.make(fragmentTripMainBinding.root, "Chip 오류 발생", Snackbar.LENGTH_SHORT).show()
//                        }
//                    }
//                    holder.textViewTripMainRowHashTag.visibility = View.VISIBLE
//                    holder.textViewTripMainRowHashTag.text = tripPostItemList[position].tripPostHashTag
//                }
//            }
//            //선택된 탭이 "찜목록"일 경우
//            else {
//                Log.d("ㅁㅇ", "찜목록")
//                holder.textViewTripMainRowTitle.text = tripPostItemList[position].tripPostTitle
//                if(tripPostItemList[position].tripPostDate!![1] == null) {
//                    holder.textViewNotificationDate.text = tripPostItemList[position].tripPostDate!![0]
//                } else {
//                    holder.textViewNotificationDate.text = "${tripPostItemList[position].tripPostDate!![0]} ~ ${tripPostItemList[position].tripPostDate!![1]}"
//                }
//                holder.textViewTripMainRowNOP.text = tripPostItemList[position].tripPostMemberCount.toString()
//                holder.textViewTripMainRowLocation.text = tripPostItemList[position].tripPostLocationName
//                when(tripPostItemList[position].tripPostTripCategory!!.size) {
//                    1 -> {
//                        holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                        holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                        holder.chipTripMainRowCategory2.visibility = View.INVISIBLE
//                        holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
//                    }
//                    2 -> {
//                        holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                        holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                        holder.chipTripMainRowCategory2.text = tripPostItemList[position].tripPostTripCategory!![1]
//                        holder.chipTripMainRowCategory2.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![1])
//                        holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
//                    }
//                    3 -> {
//                        holder.chipTripMainRowCategory1.text = tripPostItemList[position].tripPostTripCategory!![0]
//                        holder.chipTripMainRowCategory1.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![0])
//                        holder.chipTripMainRowCategory2.text = tripPostItemList[position].tripPostTripCategory!![1]
//                        holder.chipTripMainRowCategory2.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![1])
//                        holder.chipTripMainRowCategory3.text = tripPostItemList[position].tripPostTripCategory!![2]
//                        holder.chipTripMainRowCategory3.chipIcon = chipIcon(tripPostItemList[position].tripPostTripCategory!![2])
//                    }
//                    else -> {
//                        Snackbar.make(fragmentTripMainBinding.root, "Chip 오류 발생", Snackbar.LENGTH_SHORT).show()
//                    }
//                }
//
//                holder.textViewTripMainRowHashTag.visibility = View.VISIBLE
//                holder.textViewTripMainRowHashTag.text = tripPostItemList[position].tripPostHashTag
//            }
//        }
//    }

//    fun initTripViewModel() {
//        tripPostViewModel = ViewModelProvider(this)[TripPostViewModel::class.java]
//        tripPostViewModel.getTripPostData()
//
//        tripPostViewModel.tripPostList.observe(viewLifecycleOwner){
//            if(it != null) {
//                (fragmentTripMainBinding.recyclerViewTripMain.adapter as? TripMainFragment.TripMainAdapter)?.updateItemList(it)
//            }
//        }
//    }

//    // chip 아이콘
//    fun chipIcon(chipCategory: String): Drawable? {
//        var drawable: Drawable? = null
//        when(chipCategory) {
//            "맛집 탐방" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.restaurant_20px)
//            }
//            "휴양" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.breaktime_20px)
//            }
//            "관광" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.tour_20px)
//            }
//            "축제" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.festival_20px)
//            }
//            "자연" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.forest_20px)
//            }
//            "쇼핑" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.shopping_bag_20px)
//            }
//            "액티비티" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.activity_20px)
//            }
//            "사진촬영" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.photo_camera_20px)
//            }
//            "스포츠" -> {
//                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.sports_soccer_20px)
//            }
//
//        }
//        return drawable
//    }
}