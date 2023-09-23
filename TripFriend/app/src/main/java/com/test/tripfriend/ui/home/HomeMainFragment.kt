package com.test.tripfriend.ui.home

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.BottomSheetMainFilterBinding
import com.test.tripfriend.databinding.FragmentHomeMainBinding
import com.test.tripfriend.repository.UserRepository
import com.test.tripfriend.ui.user.LoginMainActivity
import com.test.tripfriend.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeMainFragment : Fragment() {
    lateinit var fragmentHomeMainBinding: FragmentHomeMainBinding
    lateinit var mainActivity: MainActivity
    lateinit var loginMainActivity: LoginMainActivity
    lateinit var viewPager: ViewPager2
    lateinit var viewPagerAdapter: HomeMainFragment.ViewPagerAdapter
    lateinit var bottomSheetMainFilterBinding: BottomSheetMainFilterBinding
    lateinit var homeViewModel: HomeViewModel


    val spinnerList = arrayOf(
        "제목+내용", "해시태그"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeMainBinding = FragmentHomeMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        bottomSheetMainFilterBinding = BottomSheetMainFilterBinding.inflate(layoutInflater)

        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE

        homeViewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]

        val sharedPreferences =
            mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val userClass = UserRepository.getUserInfo(sharedPreferences)

        viewPager = fragmentHomeMainBinding.viewPager2HomeMain
        viewPagerAdapter = ViewPagerAdapter(mainActivity)
        viewPager.adapter = viewPagerAdapter

        fragmentHomeMainBinding.run {

            // 스피너
            spinnerHomeMainSearch.run {
                spinnerClick()
            }

            textInputEditTextHomeMain.run {
                addTextChangedListener {
                    val searchFilter = spinnerList[spinnerHomeMainSearch.selectedItemPosition]
                    lifecycleScope.launch {
                        delay(500)
                        homeViewModel.getSearchedPostList(it.toString(), searchFilter)
                    }
                }
                setOnEditorActionListener { textView, action, keyEvent ->
                    if (action == EditorInfo.IME_ACTION_DONE) {
                        // 키보드 내리기
                        val inputMethodManager = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(textInputEditTextHomeMain.windowToken, 0)
                        true
                    }
                    false
                }
            }

            // 필터
            imageButtonHomeMainFilter.run {
                setOnClickListener {
                    val modalBottomSheet = ModalBottomSheet()
                    modalBottomSheet.show(mainActivity.supportFragmentManager, ModalBottomSheet.TAG)
                }
            }

            // 뷰페이저
            viewPager2HomeMain.run {
                viewPager2HomeMain.adapter = adapter
                isUserInputEnabled = false

                TabLayoutMediator(tabLayoutHomeMain, viewPager2HomeMain) { tab, position ->
                    when (position) {
                        0 -> tab.text = "목록"
                        1 -> tab.text = "지도"
                    }
                }.attach()
                // 탭의 텍스트 색상 변경
                val tabLayout = fragmentHomeMainBinding.tabLayoutHomeMain
                val selectedColor = ContextCompat.getColor(requireContext(), R.color.highLightColor)
                val unselectedColor = ContextCompat.getColor(requireContext(), R.color.gray)

                // 선택되지 않은 탭의 텍스트 색상 설정
                tabLayout.setTabTextColors(unselectedColor, selectedColor)
                // 선택된 탭의 텍스트 색상 설정
                tabLayout.setSelectedTabIndicatorColor(selectedColor)
            }


            // 플로팅버튼
            floatingActionButtonHomeMain.run {
                setOnClickListener {
                    if (userClass.userEmail == "NoneUserEmail") {
                        MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).apply {
                            setMessage("로그인한 회원만 이용하실 수 있는 서비스입니다. \n로그인 하시겠습니까?")
                            setNegativeButton("취소", null)
                            setPositiveButton("로그인") { dialogInterface: DialogInterface, i: Int ->
                                val intent = Intent(mainActivity, LoginMainActivity::class.java)
                                startActivity(intent)
                                mainActivity.removeFragment(MainActivity.HOME_MAIN_FRAGMENT)
                            }
                            show()
                            return@setOnClickListener
                        }
                    }

                    mainActivity.replaceFragment(
                        MainActivity.ACCOMPANY_REGISTER_FRAGMENT1,
                        true,
                        true,
                        null
                    )
                }
            }
        }

        return fragmentHomeMainBinding.root
    }

    // 스피너 기능
    fun spinnerClick() {
        fragmentHomeMainBinding.run {
            spinnerHomeMainSearch.run {
                // 값을 가져올때는 spinnerHomeMainSearch.selectedItemPosition 으로 인덱스 가져와
                // spinnerList 에 접근해서 가져오기

                // 스피너 어댑터 설정
                val a1 = ArrayAdapter(
                    mainActivity,
                    // Spinner가 접혀있을 때 모양
                    R.layout.spinner_row,
                    spinnerList
                )
                a1.setDropDownViewResource(R.layout.spinner_row)
                adapter = a1

                // Spinner의 항목을 코드로 선택한다.
                // 0부터 시작하는 순서값을 넣어준다.
                setSelection(0)

                // 항목을 선택하면 동작하는 리스너
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    // 항목을 선택했을 호출되는 메서드
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }

    // 뷰 페이저 어댑터
    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        val fragments: List<Fragment> = listOf(HomeListFragment(), HomeMapFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            val resultFragment = when (position) {
                0 -> HomeListFragment()
                1 -> HomeMapFragment()
                else -> HomeListFragment()
            }
            return resultFragment
        }
    }

    override fun onResume() {
        super.onResume()
        viewPagerAdapter = ViewPagerAdapter(mainActivity)
        viewPager.adapter = viewPagerAdapter
        viewPager.setCurrentItem(mainActivity.homeMainPosition, false)
    }

    // 필터 바텀시트
    class ModalBottomSheet : BottomSheetDialogFragment() {
        lateinit var bottomSheetMainFilterBinding: BottomSheetMainFilterBinding
        lateinit var mainActivity: MainActivity

        // 최대 선택 가능 Chip 갯수
        val maxSelectableChips = 3

        // 칩 카운트 변수
        var chipCount = 0

        val categoryList = mutableListOf<String>()
        val genderList = mutableListOf<Boolean>()
        val dateList = IntArray(2) {0}

        lateinit var homeViewModel: HomeViewModel

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            bottomSheetMainFilterBinding = BottomSheetMainFilterBinding.inflate(layoutInflater)
            return bottomSheetMainFilterBinding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            mainActivity = activity as MainActivity

            var firstDate = ""
            var secondDate = ""

            bottomSheetMainFilterBinding.run {

                // 초기화
                buttonDialogFilterReset.setOnClickListener {
                    calendarTripMain.resetAllSelectedViews()
                    unCheckChips()
                }

                chipMax(chipDialogFilterCategory1)
                chipMax(chipDialogFilterCategory2)
                chipMax(chipDialogFilterCategory3)
                chipMax(chipDialogFilterCategory4)
                chipMax(chipDialogFilterCategory5)
                chipMax(chipDialogFilterCategory6)
                chipMax(chipDialogFilterCategory7)
                chipMax(chipDialogFilterCategory8)
                chipMax(chipDialogFilterCategory9)

                // 데이트 피커
                calendarTripMain.run {
                    val calendar = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentDate = Date()
                    val todayDate = dateFormat.format(currentDate)

                    calendar.time = currentDate
                    calendar.add(Calendar.YEAR, 2)

                    setSelectableDateRange(dateToCalendar(todayDate), calendar)

                    setCalendarListener(
                        object :
                            CalendarListener {
                            override fun onFirstDateSelected(startDate: Calendar) {
                                val date = startDate.time
                                val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

                                firstDate = format.format(date)
                                secondDate = format.format(date)
                            }

                            override fun onDateRangeSelected(
                                startDate: Calendar,
                                endDate: Calendar
                            ) {
                                val startDate = startDate.time
                                val endDate = endDate.time
                                val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

                                firstDate = format.format(startDate)
                                secondDate = format.format(endDate)
                            }
                        })
                }

                buttonHomeMainFilterCancel.setOnClickListener {
                    dismiss()
                }

                buttonHomeMainFilterApply.setOnClickListener {
                    categoryCheck()
                    genderList.add(chipDialogFilterGender1.isChecked)
                    genderList.add(chipDialogFilterGender2.isChecked)

                    if(firstDate.isNotEmpty() && secondDate.isNotEmpty()) {
                        dateList[0] = firstDate.toInt()
                        dateList[1] = secondDate.toInt()
                    }

                    homeViewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]
                    homeViewModel.getFilteredPostList(categoryList,genderList, dateList)
                    dismiss()
                }
            }
        }

        private fun categoryCheck() {
            bottomSheetMainFilterBinding.run {
                val chipArray = arrayOf(
                    chipDialogFilterCategory1,
                    chipDialogFilterCategory2,
                    chipDialogFilterCategory3,
                    chipDialogFilterCategory4,
                    chipDialogFilterCategory5,
                    chipDialogFilterCategory6,
                    chipDialogFilterCategory7,
                    chipDialogFilterCategory8,
                    chipDialogFilterCategory9
                )
                chipArray.forEach { chip ->
                    if (chip.isChecked == true) {
                        categoryList.add(chip.text.toString())
                    }
                }
            }
        }

        private fun unCheckChips() {

            bottomSheetMainFilterBinding.run {
                val chipArray = arrayOf(
                    chipDialogFilterCategory1,
                    chipDialogFilterCategory2,
                    chipDialogFilterCategory3,
                    chipDialogFilterCategory4,
                    chipDialogFilterCategory5,
                    chipDialogFilterCategory6,
                    chipDialogFilterCategory7,
                    chipDialogFilterCategory8,
                    chipDialogFilterCategory9,
                    chipDialogFilterGender1,
                    chipDialogFilterGender2,
                )
                chipArray.forEach { chip ->
                    chip.isChecked = false
                }
                chipCount = 0
            }
        }

        companion object {
            const val TAG = "ModalBottomSheet"
        }

        // 최대 3개 이상의 칩을 선택 못하게 하는 함수
        private fun chipMax(chipId: Chip) {
            chipId.setOnClickListener {
                if (chipId.isChecked) {
                    if (chipCount >= maxSelectableChips) {
                        chipId.isChecked = false
                        Snackbar.make(
                            bottomSheetMainFilterBinding.root,
                            "여행 카테고리는 최대 3개 선택 가능합니다.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        chipCount++
                    }
                } else {
                    chipCount--
                }
            }
        }

        private fun dateToCalendar(dateString: String): Calendar {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date: Date? = sdf.parse(dateString)

            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            return calendar
        }

    }
}