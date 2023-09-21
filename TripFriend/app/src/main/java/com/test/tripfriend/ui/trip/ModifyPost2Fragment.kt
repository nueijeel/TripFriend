package com.test.tripfriend.ui.trip

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentModifyPost2Binding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.tripfriend.viewmodel.TripPostViewModel
import java.util.Date

class ModifyPost2Fragment : Fragment() {
    lateinit var fragmentModifyPost2Binding: FragmentModifyPost2Binding
    lateinit var mainActivity: MainActivity

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    lateinit var tripPostViewModel: TripPostViewModel

    var imageUri : Uri? = null
    var dates = mutableListOf<String>()
    var firstDate = ""
    var secondDate = ""
    var tripPostIdx = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyPost2Binding = FragmentModifyPost2Binding.inflate(layoutInflater)

        //앨범 런처 초기화
        albumLauncher = albumSetting(fragmentModifyPost2Binding.imageViewModifyPost2)

        // 이전 화면 정보 가져오기
        val tripPostDocumentId = arguments?.getString("tripPostDocumentId")!!
        val country = arguments?.getString("country")
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        //뷰모델
        tripPostViewModel = ViewModelProvider(mainActivity)[TripPostViewModel::class.java]

        tripPostViewModel.tripPostList.observe(viewLifecycleOwner) { tripPost ->
            tripPostIdx = tripPost.tripPostIdx.toLong()
            fragmentModifyPost2Binding.run {
                textInputEditTextModifyPost2Title.setText(tripPost.tripPostTitle)

                textInputEditTextModifyPost2NOP.setText(tripPost.tripPostMemberCount.toString())

                textInputEditTextModifyPost2Content.setText(tripPost.tripPostContent)

                textInputEditTextModifyPost2Date.setText("여행 날짜 : ${formatDate(tripPost.tripPostDate!![0])} ~ ${formatDate(tripPost.tripPostDate!![1])}")
                firstDate = tripPost.tripPostDate!![0]
                secondDate = tripPost.tripPostDate!![1]

                tripPostIdx = tripPost.tripPostIdx.toLong()

                imageUri = Uri.parse(tripPost.tripPostImage!!)

                // 동행글 이미지 가져오기
                if(tripPost.tripPostImage!!.isNotEmpty()) {
                    tripPostViewModel.getTripPostImage(tripPost.tripPostImage)
                }
            }
        }

        // 이미지 라이브데이터
        tripPostViewModel.tripPostImage.observe(viewLifecycleOwner) {uri ->
            if(uri != null) {
                Glide.with(mainActivity).load(uri)
                    .error(R.drawable.login_background_image)
                    .into(fragmentModifyPost2Binding.imageViewModifyPost2)
            } else {
                fragmentModifyPost2Binding.imageViewModifyPost2.setImageResource(R.drawable.login_background_image)
            }
        }

        // 문서id로 해당 동행글 정보 가져오기
        tripPostViewModel.getSelectDocumentData(tripPostDocumentId)

        fragmentModifyPost2Binding.run {
            materialToolbarModifyPost2.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODFY_POST2_FRAGMENT)
                }
            }

            mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

            // 이미지 앨범
            imageViewModifyPost2.setOnClickListener {
                //앨범 이동
                val albumIntent = Intent(Intent.ACTION_PICK)
                albumIntent.setType("image/*")

                val mimeType = arrayOf("image/*")
                albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                albumLauncher.launch(albumIntent)
            }

            // 달력
            textInputLayoutModifyPost2Date.run {
                // 데이트 피커
                fragmentModifyPost2Binding.calendarModifyPost2.run {
                    val calendar = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentDate = Date()
                    val todayDate = dateFormat.format(currentDate)

                    calendar.time = currentDate
                    calendar.add(Calendar.YEAR, 2)

                    setSelectableDateRange(dateToCalendar(todayDate), calendar)

                    setCalendarListener(object :
                        CalendarListener {
                        override fun onFirstDateSelected(startDate: Calendar) {
                            val date = startDate.time
                            val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

                            firstDate = format.format(date)
                            secondDate = format.format(date)
//                        Toast.makeText(mainActivity, "Start Date: " + format.format(date), Toast.LENGTH_SHORT).show()
                        }

                        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                            val startDate = startDate.time
                            val endDate = endDate.time
                            val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

                            firstDate = format.format(startDate)
                            secondDate = format.format(endDate)
                        }
                    })
                }
            }
            
            // 다음버튼 - 유효성 검사 및 다음 화면에 정보 전달
            buttonAccompanyModifyPost2ToNextView.setOnClickListener {
                if(textInputEditTextModifyPost2Title.text?.length == 0) {
                    MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).apply {
                        setTitle("제목 입력")
                        setMessage("게시글의 제목을 입력해주세요.")
                        setNegativeButton("닫기", null)
                        show()
                        return@setOnClickListener
                    }
                }

                if(textInputEditTextModifyPost2NOP.text?.length == 0) {
                    MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).apply {
                        setTitle("인원 입력")
                        setMessage("동행 인원을 입력해주세요.")
                        setNegativeButton("닫기", null)
                        show()
                        return@setOnClickListener
                    }
                }

                if(textInputEditTextModifyPost2Content.text?.length == 0) {
                    MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).apply {
                        setTitle("내용 입력")
                        setMessage("내용을 입력해주세요.")
                        setNegativeButton("닫기", null)
                        show()
                        return@setOnClickListener
                    }
                }

                val title = textInputEditTextModifyPost2Title.text.toString()
                val people = textInputEditTextModifyPost2NOP.text.toString()
                val content = textInputEditTextModifyPost2Content.text.toString()

                // 이미지 파일 경로
                val postImagePath = "TripPost/$tripPostIdx"

                // 다음 화면으로 정보 전달
                val newBundle = Bundle()

                newBundle.putString("tripPostDocumentId", tripPostDocumentId)
                newBundle.putString("country", country)
                newBundle.putString("title", title)
                newBundle.putString("postImagePath", postImagePath)
                newBundle.putString("people", people)
                newBundle.putString("content", content)
                newBundle.putLong("tripPostIdx", tripPostIdx)
                newBundle.putString("startDate", firstDate)
                newBundle.putString("endDate", secondDate)
                newBundle.putString("image", imageUri.toString())
                if (latitude != null && longitude != null) {
                    newBundle.putDouble("latitude", latitude)
                    newBundle.putDouble("longitude", longitude)
                }

                mainActivity.replaceFragment(MainActivity.MODFY_POST3_FRAGMENT,true,true, newBundle)
            }
        }

        return fragmentModifyPost2Binding.root
    }

    private fun dateToCalendar(dateString: String): Calendar {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date: Date? = sdf.parse(dateString)

        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }

        Log.d("qwer", "calendar $calendar")
        return calendar
    }

    override fun onPause() {
        super.onPause()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }

    // 앨범 설정 함수
    fun albumSetting(imageView : ImageView) : ActivityResultLauncher<Intent> {
        //앨범에서 이미지 가져오기
        val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //이미지 가져오기 성공
            if(it.resultCode == Activity.RESULT_OK){
                it.data?.data?.let { uri ->
                    if(uri != null){
                        imageUri = uri
                        setImage(uri, imageView)
                    }
                }
            }
        }
        return albumLauncher
    }

    //uri를 이미지뷰에 셋팅하는 함수
    fun setImage(image: Uri, imageView: ImageView){
        val inputStream = mainActivity.contentResolver.openInputStream(image)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        //회전 각도값을 가져옴
        val degree = getDegree(image)

        //회전 이미지를 생성한다
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotateBitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, false)

        //글라이드 라이브러리로 view에 이미지 출력
        Glide.with(mainActivity).load(rotateBitmap)
            .into(imageView)
    }

    // 이미지 파일에 기록되어 있는 회전 정보를 가져온다.
    fun getDegree(uri: Uri) : Int{
        var exifInterface: ExifInterface? = null

        // 사진 파일로 부터 tag 정보를 관리하는 객체를 추출한다.
        try {
            val inputStream = mainActivity.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                exifInterface = ExifInterface(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var degree = 0
        if(exifInterface != null){
            // 각도 값을 가지고온다.
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

            when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        }
        return degree
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
}