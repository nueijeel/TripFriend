package com.test.tripfriend.ui.home

import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.test.tripfriend.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.DialogHomeMainFilterBinding
import com.test.tripfriend.databinding.FragmentHomeMainBinding

class HomeMainFragment : Fragment() {
    lateinit var fragmentHomeMainBinding: FragmentHomeMainBinding
    lateinit var mainActivity: MainActivity

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


        fragmentHomeMainBinding.run {
            // 스피너
            spinnerHomeMainSearch.run {
                spinnerClick()
            }

            // 필터 다이얼로그 - 참조(https://magicalcode.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BD%94%ED%8B%80%EB%A6%B0-Custom-Dialog)
            imageButtonHomeMainFilter.run {
                setOnClickListener {
                    DialogFilter().dig()
                }
            }

            // 뷰페이저
            viewPager2HomeMain.run {
                val adapter = ViewPagerAdapter(mainActivity)
                viewPager2HomeMain.adapter = adapter

                TabLayoutMediator(tabLayoutHomeMain, viewPager2HomeMain) { tab, position ->
                    when(position) {
                        0 -> tab.text = "목록"
                        1 -> tab.text = "지도"
                    }
                }.attach()
            }

            // 플로팅버튼
            floatingActionButtonHomeMain.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.ACCOMPANYREGISTERFRAGMENT1, true, true, null)
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
                    android.R.layout.simple_spinner_item,
                    spinnerList
                )
                a1.setDropDownViewResource(android.R.layout.simple_spinner_item)
                adapter = a1

                // Spinner의 항목을 코드로 선택한다.
                // 0부터 시작하는 순서값을 넣어준다.
                setSelection(0)

                // 항목을 선택하면 동작하는 리스너
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    // 항목을 선택했을 호출되는 메서드
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }

    // 뷰 페이저 어댑터
    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        val fragments: List<Fragment> = listOf(HomeListFragment(), HomeMapFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            val resultFragment = when(position){
                0 -> HomeListFragment()
                1 -> HomeMapFragment()
                else -> HomeListFragment()
            }
            return resultFragment
        }
    }

    // 다이얼로그
    inner class DialogFilter() {
        val dialog = Dialog(mainActivity)

        fun dig() {
            val dialogHomeMainFilterBinding : DialogHomeMainFilterBinding

            dialogHomeMainFilterBinding = DialogHomeMainFilterBinding.inflate(layoutInflater)
            dialog.setContentView(dialogHomeMainFilterBinding.root)

            dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(true)

            dialogHomeMainFilterBinding.run {
                buttonDialogFilterCancel.setOnClickListener {
                    dialog.dismiss()
                }

                buttonDialogFilterDone.setOnClickListener {
                    dialog.dismiss()
                }
            }


            dialog.show()
        }
    }
}