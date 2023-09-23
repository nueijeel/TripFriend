package com.test.tripfriend.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.test.tripfriend.R
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.databinding.FragmentHomeListBinding
import com.test.tripfriend.databinding.RowHomeListBinding
import com.test.tripfriend.databinding.RowTripMainBinding
import com.test.tripfriend.dataclassmodel.TripPost
import com.test.tripfriend.repository.UserRepository
import com.test.tripfriend.ui.trip.InProgressFragment
import com.test.tripfriend.ui.trip.TripMainFragment
import com.test.tripfriend.viewmodel.HomeViewModel
import com.test.tripfriend.viewmodel.TripPostViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeListFragment : Fragment() {
    lateinit var fragmentHomeListBinding: FragmentHomeListBinding
    lateinit var mainActivity: MainActivity

    lateinit var homeViewModel: HomeViewModel
    lateinit var currentUserEmail : String
    val tripPostViewModel = TripPostViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeListBinding = FragmentHomeListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // 로그인 중인 사용자 정보
        val sharedPreferences = mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val userClass = UserRepository.getUserInfo(sharedPreferences)
        currentUserEmail = userClass.userEmail

        initHomeViewModel()

        fragmentHomeListBinding.run {
            refreshLayout01.setOnRefreshListener {
                initHomeViewModel()
                fragmentHomeListBinding.refreshLayout01.isRefreshing = false
            }
            recyclerViewHomeList.run {
                adapter = HomeListAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        return fragmentHomeListBinding.root
    }

    inner class HomeListAdapter : RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>() {
        var homePostItemList: List<TripPost> = emptyList()

        fun updateItemList(newList: List<TripPost>) {
            this.homePostItemList = newList
            notifyDataSetChanged()
        }

        val sharedPreferences =
            mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val userClass = UserRepository.getUserInfo(sharedPreferences)

        inner class HomeListViewHolder(rowTripMainBinding: RowTripMainBinding) :
            RecyclerView.ViewHolder(rowTripMainBinding.root) {
            val textViewTripMainRowTitle: TextView // 제목
            val textViewNotificationDate: TextView // 날짜
            val textViewTripMainRowNOP: TextView // 인원
            val textViewTripMainRowLocation: TextView // 지역
            val chipTripMainRowCategory1: Chip // 카테고리1
            val chipTripMainRowCategory2: Chip // 카테고리2
            val chipTripMainRowCategory3: Chip // 카테고리3
            val textViewTripMainRowHashTag: TextView // 해시태그
            val textViewTripMainRowLikedCount: TextView // 좋아요 수
            val imageViewTripMainRowLiked: ImageView // 좋아요 아이콘

            init {
                textViewTripMainRowTitle = rowTripMainBinding.textViewTripMainRowTitle
                textViewNotificationDate = rowTripMainBinding.textViewNotificationDate
                textViewTripMainRowNOP = rowTripMainBinding.textViewTripMainRowNOP
                textViewTripMainRowLocation = rowTripMainBinding.textViewTripMainRowLocation
                chipTripMainRowCategory1 = rowTripMainBinding.chipTripMainRowCategory1
                chipTripMainRowCategory2 = rowTripMainBinding.chipTripMainRowCategory2
                chipTripMainRowCategory3 = rowTripMainBinding.chipTripMainRowCategory3
                textViewTripMainRowHashTag = rowTripMainBinding.textViewTripMainRowHashTag
                textViewTripMainRowLikedCount = rowTripMainBinding.textViewTripMainRowLikedCount
                imageViewTripMainRowLiked = rowTripMainBinding.imageViewTripMainRowLiked

                rowTripMainBinding.root.setOnClickListener {
                    val currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                    val formattedDate = currentDate.format(formatter)
                    val newBundle = Bundle()
                    UserRepository.getAllUser {
                        for (document in it.result.documents) {
                            if(document.getString("userEmail") == homePostItemList[adapterPosition].tripPostWriterEmail) {
                                val userProfilePath = document.getString("userProfilePath").toString()
                                Log.d("aaaa","userProfilePath = ${userProfilePath}")
                                newBundle.putString("tripPostWriterEmail", homePostItemList[adapterPosition].tripPostWriterEmail) // 작성자 이메일
                                newBundle.putString("userProfilePath",userProfilePath)
                                newBundle.putString("tripPostDocumentId", homePostItemList[adapterPosition].tripPostDocumentId)   // 문서아이디
                                newBundle.putString("tripPostImage", homePostItemList[adapterPosition].tripPostImage) //이미지 경로
                                Log.d("aaaa","tripPostImage = ${homePostItemList[adapterPosition].tripPostImage}")

                                if(homePostItemList[adapterPosition].tripPostMemberList?.contains(userClass.userNickname) == true) {   // 참여중인 동행글인 경우
                                    if(homePostItemList[adapterPosition].tripPostDate?.get(1)!! < formattedDate) {  // 지난 동행
                                        newBundle.putString("viewState", "Pass")
                                    } else {
                                        newBundle.putString("viewState", "InProgress")
                                    }
                                } else {    // 미 참여
                                    if(homePostItemList[adapterPosition].tripPostDate?.get(1)!! < formattedDate) {  // 지난 동행
                                        newBundle.putString("viewState", "HomeListPass")
                                    } else {
                                        newBundle.putString("viewState", "HomeList")
                                    }
                                }

                                mainActivity.replaceFragment(MainActivity.READ_POST_FRAGMENT, true, true, newBundle)
                            }
                        }
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
            val rowTripMainBinding = RowTripMainBinding.inflate(layoutInflater)

            rowTripMainBinding.root.run {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            return HomeListViewHolder(rowTripMainBinding)
        }

        override fun getItemCount(): Int {
            return homePostItemList.size
        }

        override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
            holder.textViewTripMainRowTitle.text = homePostItemList[position].tripPostTitle

            if (homePostItemList[position].tripPostDate!![1] == null) {
                holder.textViewNotificationDate.text = formatDate(homePostItemList[position].tripPostDate!![0])
            } else {
                holder.textViewNotificationDate.text =
                    "${formatDate(homePostItemList[position].tripPostDate!![0])} ~ ${
                        formatDate(homePostItemList[position].tripPostDate!![1])
                    }"
            }

            holder.textViewTripMainRowNOP.text =
                homePostItemList[position].tripPostMemberCount.toString()
            holder.textViewTripMainRowLocation.text =
                homePostItemList[position].tripPostLocationName

            for(email in homePostItemList[position].tripPostLiked!!) {
                if(email == currentUserEmail) {
                    holder.imageViewTripMainRowLiked.setImageResource(R.drawable.favorite_fill_24px)
                }
            }

            when (homePostItemList[position].tripPostTripCategory!!.size) {
                1 -> {
                    holder.chipTripMainRowCategory1.text =
                        homePostItemList[position].tripPostTripCategory!![0]
                    holder.chipTripMainRowCategory1.chipIcon =
                        chipIcon(homePostItemList[position].tripPostTripCategory!![0])
                    holder.chipTripMainRowCategory2.visibility = View.INVISIBLE
                    holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
                }

                2 -> {
                    holder.chipTripMainRowCategory1.text =
                        homePostItemList[position].tripPostTripCategory!![0]
                    holder.chipTripMainRowCategory1.chipIcon =
                        chipIcon(homePostItemList[position].tripPostTripCategory!![0])
                    holder.chipTripMainRowCategory2.text =
                        homePostItemList[position].tripPostTripCategory!![1]
                    holder.chipTripMainRowCategory2.chipIcon =
                        chipIcon(homePostItemList[position].tripPostTripCategory!![1])
                    holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
                }

                3 -> {
                    holder.chipTripMainRowCategory1.text =
                        homePostItemList[position].tripPostTripCategory!![0]
                    holder.chipTripMainRowCategory1.chipIcon =
                        chipIcon(homePostItemList[position].tripPostTripCategory!![0])
                    holder.chipTripMainRowCategory2.text =
                        homePostItemList[position].tripPostTripCategory!![1]
                    holder.chipTripMainRowCategory2.chipIcon =
                        chipIcon(homePostItemList[position].tripPostTripCategory!![1])
                    holder.chipTripMainRowCategory3.text =
                        homePostItemList[position].tripPostTripCategory!![2]
                    holder.chipTripMainRowCategory3.chipIcon =
                        chipIcon(homePostItemList[position].tripPostTripCategory!![2])
                }

                else -> {
                    holder.chipTripMainRowCategory1.visibility = View.INVISIBLE
                    holder.chipTripMainRowCategory2.visibility = View.INVISIBLE
                    holder.chipTripMainRowCategory3.visibility = View.INVISIBLE
                }
            }

            holder.textViewTripMainRowHashTag.visibility = View.VISIBLE
            holder.textViewTripMainRowHashTag.text = homePostItemList[position].tripPostHashTag
            holder.textViewTripMainRowLikedCount.text =
                homePostItemList[position].tripPostLiked!!.size.toString()
        }
    }

    fun initHomeViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.getTripPostData()

        homeViewModel.tripPostList.observe(viewLifecycleOwner) {
//            fragmentHomeListBinding.textViewHomeListNoPost.visibility = View.GONE
//            (fragmentHomeListBinding.recyclerViewHomeList.adapter as? HomeListAdapter)?.updateItemList(it)
            if(it != null) {
                fragmentHomeListBinding.textViewHomeListNoPost.visibility = View.GONE
                (fragmentHomeListBinding.recyclerViewHomeList.adapter as? HomeListAdapter)?.updateItemList(it)
            } else {
                fragmentHomeListBinding.textViewHomeListNoPost.visibility = View.VISIBLE
                fragmentHomeListBinding.textViewHomeListNoPost.text = "동행 중인 여행이 없습니다."
            }
        }
    }

    // chip 아이콘
    fun chipIcon(chipCategory: String): Drawable? {
        var drawable: Drawable? = null
        when (chipCategory) {
            "맛집 탐방" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.restaurant_20px)
            }

            "휴양" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.breaktime_20px)
            }

            "관광" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.tour_20px)
            }

            "축제" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.festival_20px)
            }

            "자연" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.forest_20px)
            }

            "쇼핑" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.shopping_bag_20px)
            }

            "액티비티" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.activity_20px)
            }

            "사진촬영" -> {
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.photo_camera_20px)
            }

            "스포츠" -> {
                drawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.sports_soccer_20px)
            }
        }
        return drawable
    }

    // 날짜 형식 변환
    fun formatDate(date: String): String {
        if (date != "") {
            val year = date.substring(0, 4)
            val month = date.substring(4, 6)
            val day = date.substring(6, 8)

            val formattedDate = "$year-$month-$day"

            return formattedDate
        }
        return ""
    }

    override fun onResume() {
        super.onResume()
        mainActivity.homeMainPosition = 0
    }

    override fun onStop() {
        super.onStop()
    }

}