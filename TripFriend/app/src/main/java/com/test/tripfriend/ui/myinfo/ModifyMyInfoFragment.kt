package com.test.tripfriend.ui.myinfo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentModifyMyInfoBinding
import com.test.tripfriend.repository.UserRepository
import com.test.tripfriend.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking

class ModifyMyInfoFragment : Fragment() {

    lateinit var fragmentModifyMyInfoBinding: FragmentModifyMyInfoBinding
    lateinit var mainActivity: MainActivity

    lateinit var userViewModel : UserViewModel

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    var nicknameIsChecked:Boolean = true
    var profileImage : Uri? = null

    val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        fragmentModifyMyInfoBinding = FragmentModifyMyInfoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        //앨범 런처 초기화
        albumLauncher = albumSetting(fragmentModifyMyInfoBinding.imageViewMyProfile)

        //로그인 된 유저의 정보로 변경 필
        val testUserEmail = "nueijeel0423@gmail.com"
        val testUserAuthentication = "이메일"

        fragmentModifyMyInfoBinding.run {

            toolbarModifyMyInfo.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationIconTint(Color.BLACK)
                //툴바 뒤로가기
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MODIFY_MY_INFO_FRAGMENT)
                }
            }
            spinnerMbti.run {
                val mbti = resources.getStringArray(R.array.spinner_mbti_list)
                val spinnerAdapter = ArrayAdapter(mainActivity,R.layout.spinner_row, mbti)
                adapter = spinnerAdapter
            }

            imageViewModifyImage.setOnClickListener {
                val albumIntent = Intent(Intent.ACTION_PICK)
                albumIntent.setType("image/*")

                val mimeType = arrayOf("image/*")
                albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                albumLauncher.launch(albumIntent)
            }

            buttonNickNameCheck.setOnClickListener {
                //닉네임 중복 검사해서 중복이면 textViewWarningNickName 의 visible=true, textViewCompleteCheck visible=false
                //닉네임 중복 검사해서 중복이 아니면 textViewWarningNickName 의 visible=false,textViewCompleteCheck visible=true, nicknameIsCheck=true
                //Boolean 전역변수 하나 파서 중복 검사 통과시 true값으로 변경 후 변경 완료 버튼리스너에 해당 분기 삽입
            }

            buttonModifyMyInfo.setOnClickListener {
                val nickname = textInputEditTextModifyNickName.text.toString()
                var password = ""

                //닉네임 입력 검사
                if(textIsEmptyCheck(nickname, textInputLayoutNickName, "닉네임을 입력해주세요")){
                    return@setOnClickListener
                }

                //닉네임 중복검사 통과 했는지 확인
                if(!nicknameIsChecked) {
                    Snackbar.make(fragmentModifyMyInfoBinding.root, "닉네임 중복 검사가 필요합니다", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //이메일 로그인 및 회원가입한 회원일 경우
                if(userViewModel.user.value?.userAuthentication == "이메일"){

                    val passwordText = textInputEditTextPasswordToModify.text.toString()
                    val passwordCheckText = textInputEditTextPasswordCheck.text.toString()

                    //비밀번호 입력 검사
                    if(textIsEmptyCheck(passwordText, textInputLayoutPassword, "비밀번호를 입력해주세요")||
                        textIsEmptyCheck(passwordCheckText, textInputLayoutPasswordCheck, "비밀번호 확인을 입력해주세요")){
                        return@setOnClickListener
                    }

                    //비밀번호 일치 검사
                    if(passwordText != passwordCheckText) {
                        Snackbar.make(fragmentModifyMyInfoBinding.root, "비밀번호가 일치하지 않습니다", Snackbar.LENGTH_SHORT).show()
                        textInputEditTextPasswordCheck.setText("")
                        textInputEditTextPasswordToModify.setText("")
                        textInputLayoutPassword.requestFocus()
                        return@setOnClickListener
                    }
                    password = passwordText
                }
                val imagePath = if(userViewModel.user.value?.userProfilePath?.isEmpty()!!){
                    "UserProfileImage/$testUserEmail/1"
                }else{
                    userViewModel.user.value?.userProfilePath!!
                }

                runBlocking {
                    userRepository.updateTargetUserInfo(userViewModel.userDocumentId.value!!, imagePath, nickname, spinnerMbti.selectedItem.toString(), password)
                    //이미지 변경 됐을 때
                    if(profileImage != null){
                        //스토리지에 덮어씌우기
                        userRepository.updateTargetUserProfile(imagePath, profileImage!!)
                        Snackbar.make(fragmentModifyMyInfoBinding.root, "계정 정보가 수정되었습니다.", Snackbar.LENGTH_SHORT).show()
                        mainActivity.removeFragment(MainActivity.MODIFY_MY_INFO_FRAGMENT)
                    }
                }
            }
        }

        initViewModel(testUserEmail, testUserAuthentication)

        return fragmentModifyMyInfoBinding.root
    }

    fun initViewModel(userEmail : String, userAuthentication : String) {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.getTargetUserData(userEmail, userAuthentication)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                fragmentModifyMyInfoBinding.run {
                    //이미지
                    if(user.userProfilePath.isEmpty()){
                        //기본 이미지 지정
                        imageViewMyProfile.setImageResource(R.drawable.person_24px)
                    }
                    else{
                        //유저 정보에 저장된 이미지 지정
                        userViewModel.getTargetUserProfileImage(user.userProfilePath)
                    }

                    //닉네임
                    textInputEditTextModifyNickName.setText(user.userNickname)

                    //mbti
                    val mbti = resources.getStringArray(R.array.spinner_mbti_list)

                    for(idx in 0 until mbti.size){
                        if(user.userMBTI == "모름"){
                            spinnerMbti.setSelection(0)
                        }
                        else if(mbti[idx] == user.userMBTI){
                            spinnerMbti.setSelection(idx)
                        }
                    }

                    //pw
                    if(user.userAuthentication == "카카오" || user.userAuthentication == "네이버"){
                        textInputLayoutPassword.visibility = View.GONE
                        textInputLayoutPasswordCheck.visibility = View.GONE
                        textViewModifyMyInfoPw.visibility = View.GONE
                    }
                }
            }
        }

        userViewModel.userProfileImage.observe(viewLifecycleOwner){ uri ->
            if(uri != null){
                Glide.with(mainActivity).load(uri)
                    .into(fragmentModifyMyInfoBinding.imageViewMyProfile)
            }
            else{
                fragmentModifyMyInfoBinding.imageViewMyProfile.setImageResource(R.drawable.person_24px)
            }
        }
    }

    fun textIsEmptyCheck(text : String, textLayout : TextInputLayout, errorMessage : String) : Boolean{
        if(text.isEmpty()){
            textLayout.error = errorMessage
            textLayout.requestFocus()
            return true
        }
        else{
            textLayout.error = null
            return false
        }
    }

    fun albumSetting(imageView : ImageView) : ActivityResultLauncher<Intent> {
        //앨범에서 이미지 가져오기
        val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //이미지 가져오기 성공
            if(it.resultCode == Activity.RESULT_OK){
                it.data?.data?.let { uri ->
                    if(uri != null){
                        profileImage = uri

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
    fun getDegree(uri:Uri) : Int{
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
}