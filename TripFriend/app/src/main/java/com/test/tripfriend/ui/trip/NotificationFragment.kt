package com.test.tripfriend.ui.trip

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.tripfriend.ui.main.MainActivity
import com.google.android.material.tabs.TabLayout
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentNotificationBinding
import com.test.tripfriend.databinding.RowNotificationBinding

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
            tabLayoutNotification.run {
                val tabLayout = findViewById<TabLayout>(R.id.tabLayoutNotification)
                tabLayout.getTabAt(mainActivity.selectedTabPosition)?.select()

                //탭 선택 했을 때 밑에 줄 색상 강조색으로 변경
                setSelectedTabIndicatorColor(getResources().getColor(R.color.highLightColor))
                //탭 선택 시 선택 안된 것들의 글자 색은 회색, 선택된 것의 글자색은 강조색
                setTabTextColors(Color.GRAY, getResources().getColor(R.color.highLightColor))

                when (mainActivity.selectedTabPosition) {
                    0 -> {
                        recyclerViewNotification.run {
                            adapter = NotificationAdapter()
                            layoutManager = LinearLayoutManager(mainActivity)
                        }
                        textViewNotificationNoPost.visibility = View.GONE
                    }

                    1 -> {
                        recyclerViewNotification.run {
                            adapter = NotificationAdapter()
                            layoutManager = LinearLayoutManager(mainActivity)
                        }
                        textViewNotificationNoPost.visibility = View.GONE
//                        textViewTripMainNoPost.text = "지난 동행글 없음"
                    }
                }

                //탭을 직접 선택했을 때
                tabLayoutNotification.addOnTabSelectedListener(object :
                    TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        val selectedTabPosition = tab?.position
                        when (selectedTabPosition) {
                            0 -> {
                                mainActivity.selectedTabPosition = 0
                                recyclerViewNotification.visibility = View.VISIBLE
                                recyclerViewNotification.run {
                                    adapter = NotificationAdapter()
                                    layoutManager = LinearLayoutManager(mainActivity)
                                }
                                textViewNotificationNoPost.visibility = View.GONE
                            }

                            1 -> {
                                mainActivity.selectedTabPosition = 1
                                recyclerViewNotification.visibility = View.VISIBLE
                                textViewNotificationNoPost.visibility = View.GONE
                                recyclerViewNotification.run {
                                    adapter = NotificationAdapter()
                                    layoutManager = LinearLayoutManager(mainActivity)
                                }
//                                textViewTripMainNoPost.text = "지난 동행글 없음"
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {

                    }
                })
            }

            recyclerViewNotification.run {
                adapter = NotificationAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
            //바텀 네비 가리기
            mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE
        }

        return fragmentNotificationBinding.root
    }

    inner class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
        inner class NotificationViewHolder(rowNotificationBinding: RowNotificationBinding) : RecyclerView.ViewHolder(rowNotificationBinding.root) {
            val textViewNotificationRowTitle : TextView // 신청글 제목
            val textViewNotificationRowNickname : TextView // 신청자 이름을 포함 "~님의 동행요청임다"
            val textViewNotificationRowContent : TextView // 신청 내용
            val textViewNotificationRowShowUserProfile : TextView //프로필 보기
            val buttonNotificationRowNo : Button // 거절
            val buttonNotificationRowYes : Button // 수락

            init {
                textViewNotificationRowTitle = rowNotificationBinding.textViewNotificationRowTitle
                textViewNotificationRowNickname = rowNotificationBinding.textViewNotificationRowNicname
                textViewNotificationRowContent = rowNotificationBinding.textViewNotificationRowContent
                buttonNotificationRowNo = rowNotificationBinding.buttonNotificationRowNo
                buttonNotificationRowYes = rowNotificationBinding.buttonNotificationRowYes
                textViewNotificationRowShowUserProfile = rowNotificationBinding.textViewNotificationRowShowUserProfile

                //프로필 보기 눌렀을 때
                textViewNotificationRowShowUserProfile.setOnClickListener {
                    fragmentNotificationBinding.textViewNotificationToolbarTitle.setText("프로필 보기")
                }

                //거절 버튼 눌렀을 때
                buttonNotificationRowNo.setOnClickListener{
                    fragmentNotificationBinding.textViewNotificationToolbarTitle.setText("거절")
                }
                //수락 버튼 눌렀을 때
                buttonNotificationRowYes.setOnClickListener {
                    fragmentNotificationBinding.textViewNotificationToolbarTitle.setText("수락")
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
            val rowNotificationBinding = RowNotificationBinding.inflate(layoutInflater)

            rowNotificationBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return NotificationViewHolder(rowNotificationBinding)
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
            if (mainActivity.selectedTabPosition == 0) {
                if (position == 0) {
                    holder.textViewNotificationRowTitle.text = "추석 연휴 부산 가요~"
                    holder.textViewNotificationRowNickname.text = "hyun9님의 동행 요청입니다."
                    holder.textViewNotificationRowContent.text =
                        "국밥의 고장 부산 군침이 싹 도네 부산 여행 동참 신청합니다~! 안받아주면 지상렬"
                } else {
                    holder.textViewNotificationRowTitle.text = "보낸 동행 요청"
                    holder.textViewNotificationRowNickname.text = "hyun9님"
                    holder.textViewNotificationRowContent.text =
                        "국밥의 고장 "
                }
            }
        }
    }

    //뒤로가기 했을 때 바텀네비 다시 보여주기
    override fun onPause() {
        super.onPause()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }
}