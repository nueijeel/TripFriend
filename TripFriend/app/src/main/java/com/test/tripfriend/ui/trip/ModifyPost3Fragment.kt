package com.test.tripfriend.ui.trip

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentModifyPost3Binding
import com.test.tripfriend.dataclassmodel.TripPost
import com.test.tripfriend.repository.TripPostRepository
import com.test.tripfriend.repository.UserRepository
import com.test.tripfriend.viewmodel.TripPostViewModel
import kotlinx.coroutines.runBlocking

class ModifyPost3Fragment : Fragment() {
    lateinit var fragmentModifyPost3Binding: FragmentModifyPost3Binding
    lateinit var mainActivity: MainActivity

    lateinit var tripPostViewModel: TripPostViewModel
    val tripPostRepository = TripPostRepository()
    
    val maxSelectableChips = 3                                           // 최대 선택 가능 Chip 갯수
    var chipCount = 0                                                    // 칩 카운트 변수
    val chipGender = arrayOf(false, false)        // 성별
    var categoryChecked = false                                          // 여행 카테고리 체크 확인
    var documentId = ""                                                  // 문서 id
    var member = mutableListOf<String>()                 // 멤버 리스트
    var tripPostCategory = emptyList<String>()
    var tripPostLikedList = emptyList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyPost3Binding = FragmentModifyPost3Binding.inflate(layoutInflater)

        // 로그인 되어있는 사용자 정보 가져오기
        val sharedPreferences = mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val userClass = UserRepository.getUserInfo(sharedPreferences)

        // 이전 화면 정보 가져오기
        val tripPostDocumentId = arguments?.getString("tripPostDocumentId")!!
        val country = arguments?.getString("country")
        val title = arguments?.getString("title")
        val postImagePath = arguments?.getString("postImagePath")!!
        val people = arguments?.getString("people")
        val content = arguments?.getString("content")
        val tripPostIdx = arguments?.getLong("tripPostIdx")
        val startDate = arguments?.getString("startDate")
        val endDate = arguments?.getString("endDate")
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")
        val image = arguments?.getString("image")!!
        val imageUri = Uri.parse(image)

        documentId = tripPostDocumentId

        val date = mutableListOf<String>()

        date.add(startDate!!)
        date.add(endDate!!)

        //뷰모델
        tripPostViewModel = ViewModelProvider(mainActivity)[TripPostViewModel::class.java]

        tripPostViewModel.tripPostList.observe(viewLifecycleOwner) { tripPost ->
            fragmentModifyPost3Binding.run {
                tripPostCategory = tripPost.tripPostTripCategory!!
                // Category chip 선택
                for(chipName in tripPostCategory) {
                    when(chipName) {
                        "축제" -> {
                            chipModifyPost3Category1.isChecked = true
                        }
                        "맛집 탐방" -> {
                            chipModifyPost3Category2.isChecked = true
                        }
                        "휴양" -> {
                            chipModifyPost3Category3.isChecked = true
                        }
                        "관광" -> {
                            chipModifyPost3Category4.isChecked = true
                        }
                        "자연" -> {
                            chipModifyPost3Category5.isChecked = true
                        }
                        "쇼핑" -> {
                            chipModifyPost3Category6.isChecked = true
                        }
                        "액티비티" -> {
                            chipModifyPost3Category7.isChecked = true
                        }
                        "사진촬영" -> {
                            chipModifyPost3Category8.isChecked = true
                        }
                        "스포츠" -> {
                            chipModifyPost3Category9.isChecked = true
                        }
                    }
                }

                // 성별 chip 선택
                if(tripPost.tripPostGender[0]) { // 남자
                    chipModifyPost3Gender1.isChecked = true
                    chipModifyPost3Gender2.isChecked = false
                } else { // 여자
                    chipModifyPost3Gender1.isChecked = false
                    chipModifyPost3Gender2.isChecked = true
                }

                textInputEditTextModifyPost3Hashtag.setText(tripPost.tripPostHashTag)

                tripPostLikedList = tripPost.tripPostLiked!!
            }
        }

        // 문서id로 해당 동행글 정보 가져오기
        tripPostViewModel.getSelectDocumentData(tripPostDocumentId)

        fragmentModifyPost3Binding.run {
            materialToolbarModifyPost3.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODFY_POST3_FRAGMENT)
                }
            }

            mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

            // 칩 최대 처리
            chipMax(chipModifyPost3Category1)
            chipMax(chipModifyPost3Category2)
            chipMax(chipModifyPost3Category3)
            chipMax(chipModifyPost3Category4)
            chipMax(chipModifyPost3Category5)
            chipMax(chipModifyPost3Category6)
            chipMax(chipModifyPost3Category7)
            chipMax(chipModifyPost3Category8)
            chipMax(chipModifyPost3Category9)


            var categories = mutableListOf(
                chipModifyPost3Category1,
                chipModifyPost3Category2,
                chipModifyPost3Category3,
                chipModifyPost3Category4,
                chipModifyPost3Category5,
                chipModifyPost3Category6,
                chipModifyPost3Category7,
                chipModifyPost3Category8,
                chipModifyPost3Category9
            )

            // 칩 중복 검사
            chipModifyPost3Gender1.setOnCloseIconClickListener {
                chipModifyPost3Gender2.isChecked = false
            }
            chipModifyPost3Gender2.setOnCloseIconClickListener {
                chipModifyPost3Gender1.isChecked = false
            }

            // 완료하기 버튼 - 유효성검사 및 DB에 정보 업데이트
            buttonModifyPost3ToSubmit.setOnClickListener {
                val chipCategory = mutableListOf<String>()
                for (category in categories) {
                    if (category.isChecked) {
                        chipCategory.add(category.text.toString())
                        categoryChecked = true
                    }
                }

                member.add(userClass.userNickname)

                chipGender[0] = chipModifyPost3Gender1.isChecked
                chipGender[1] = chipModifyPost3Gender2.isChecked

                val hashTag = textInputEditTextModifyPost3Hashtag.text.toString()

                if (!categoryChecked) {
                    MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).apply {
                        setTitle("카테고리 입력")
                        setMessage("카테고리를 입력해주세요.")
                        setNegativeButton("닫기", null)
                        show()
                        return@setOnClickListener
                    }
                }

                if (!(chipGender[0] == true || chipGender[1] == true)) {
                    MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).apply {
                        setTitle("성별 입력")
                        setMessage("성별을 입력해주세요.")
                        setNegativeButton("닫기", null)
                        show()
                        return@setOnClickListener
                    }
                }

                if(textInputEditTextModifyPost3Hashtag.text.toString() == "") {
                    MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).apply {
                        setTitle("해시태그 입력")
                        setMessage("해시태그를 입력해주세요.")
                        setNegativeButton("닫기", null)
                        show()
                        return@setOnClickListener
                    }
                }

                if (title != null && country != null && content != null && tripPostIdx != null && latitude != null && longitude != null) {
                    runBlocking {
                        if(image.isNotEmpty()) {
                            val tripPost = TripPost(
                                userClass.userEmail,
                                title,
                                member,
                                people!!.toInt(),
                                postImagePath,
                                date,
                                country,
                                latitude,
                                longitude,
                                tripPostLikedList,
                                chipCategory,
                                hashTag,
                                content,
                                tripPostDocumentId,
                                tripPostIdx.toInt(),
                                chipGender.toList()
                            )
                            tripPostRepository.updateTripPostData(documentId, tripPost) {
                                // 원래 가지고있는 파이어베이스 스토리지 이미지 삭제
                                tripPostRepository.deleteTripPostImage(postImagePath) {
                                }
                                // 파이어베이스 스토리지에 이미지 추가
                                tripPostRepository.uploadTripPostImages(imageUri, postImagePath) {
                                    completePost(userClass.userEmail)
                                }
                            }
                        } else {
                            val tripPost = TripPost(
                                userClass.userEmail,
                                title,
                                member,
                                people!!.toInt(),
                                "",
                                date,
                                country,
                                latitude,
                                longitude,
                                tripPostLikedList,
                                chipCategory,
                                hashTag,
                                content,
                                tripPostDocumentId,
                                tripPostIdx.toInt(),
                                chipGender.toList()
                            )
                            tripPostRepository.updateTripPostData(documentId, tripPost) {
                                completePost(userClass.userEmail)
                            }
                        }
                    }
                }
            }
        }

        return fragmentModifyPost3Binding.root
    }

    // 최대 3개 이상의 칩을 선택 못하게 하는 함수
    fun chipMax(chipId: Chip) {
        chipId.setOnClickListener {
            if (chipId.isChecked) {
                if (chipCount >= maxSelectableChips) {
                    chipId.isChecked = false
                    Snackbar.make(fragmentModifyPost3Binding.root, "여행 카테고리는 최대 3개 선택 가능합니다.", Snackbar.LENGTH_SHORT).show()
                } else {
                    chipCount++
                }
            } else {
                chipCount--
            }
        }
    }

    // 정보 수정 완료 했을 때
    fun completePost(userEmail: String) {
        Snackbar.make(
            mainActivity.activityMainBinding.root,
            "수정이 완료되었습니다..",
            Snackbar.LENGTH_SHORT
        ).show()

        val newBundle = Bundle()
        newBundle.putString("tripPostWriterEmail", userEmail)
        newBundle.putString("tripPostDocumentId", documentId)
        newBundle.putString("viewState", "InProgress")

        mainActivity.removeFragment(MainActivity.MODFY_POST_FRAGMENT)
        mainActivity.removeFragment(MainActivity.MODFY_POST2_FRAGMENT)
        mainActivity.removeFragment(MainActivity.MODFY_POST3_FRAGMENT)

        mainActivity.replaceFragment(MainActivity.READ_POST_FRAGMENT, true, true, newBundle)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }
}