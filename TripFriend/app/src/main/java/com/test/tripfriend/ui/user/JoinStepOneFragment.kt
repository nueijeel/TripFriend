package com.test.tripfriend.ui.user

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentJoinStepOneBinding
import com.test.tripfriend.ui.main.MainActivity
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.test.tripfriend.repository.UserRepository

class JoinStepOneFragment : Fragment() {

    lateinit var fragmentJoinStepOneBinding: FragmentJoinStepOneBinding
    lateinit var loginMainActivity: LoginMainActivity
    lateinit var callback: OnBackPressedCallback
    val auth = Firebase.auth
    var pw = ""
    var email = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentJoinStepOneBinding = FragmentJoinStepOneBinding.inflate(layoutInflater)
        loginMainActivity = activity as LoginMainActivity

        fragmentJoinStepOneBinding.run {
            buttonJoinStepOneAuthCheck.visibility = View.GONE

            //툴바 설정
            materialToolbarJoinStepOne.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationIconTint(Color.BLACK)
                setNavigationOnClickListener {
                    loginMainActivity.checkEmail = 0
                    loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_ONE_FRAGMENT)
                }
            }

            //프로그래스바 설정
            progressBarJoinStepOne.run {
                //setStateNumberTypeface("nanumbarunpenregular")
            }
            buttonJoinStepOneAuth.run {
                if(loginMainActivity.checkEmail == 1){
                    visibility = View.GONE
                    buttonJoinStepOneAuthCheck.visibility = View.VISIBLE
                    buttonJoinStepOneAuthCheck.isEnabled = false
                }

                setOnClickListener {
                    //입력 하지 않았을 경우
                    if(textInputEditTextJoinStepOneEmail.text.toString() == "") {
                        val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                            setTitle("이메일 입력 오류")
                            setMessage("이메일을 입력해주세요.")
                            setNegativeButton("확인", null)
                        }
                        builder.show()
                    }
                    //입력 했을 때
                    else{
                        //형식 검사
                        if (isEmailValid(textInputEditTextJoinStepOneEmail.text.toString()) == false) {
                            val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                setTitle("이메일 입력 오류")
                                setMessage("유효하지 않은 이메일 주소입니다.")
                                setNegativeButton("확인", null)
                            }
                            builder.show()
                        }
                        else {
                            //이메일 중복검사
                            UserRepository.getAllUser() {
                                var check = 1
                                for (document in it.result.documents) {
                                    if(textInputEditTextJoinStepOneEmail.text.toString() == document.getString("userEmail")){
                                        val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                            setTitle("이메일 입력 오류")
                                            setMessage("현재 사용중인 이메일입니다.")
                                            setNegativeButton("확인", null)
                                        }
                                        builder.show()
                                        check = 0
                                        break
                                    }
                                }
                                if(check == 1){
                                    email = textInputEditTextJoinStepOneEmail.text.toString()
                                    //비밀번호 유효성 검사
                                    if(textInputEditTextJoinStepOnePw.text.toString().length < 6){
                                        val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                            setTitle("비밀번호 입력 오류")
                                            setMessage("비밀번호는 6자 이상 입력해주세요.")
                                            setNegativeButton("확인", null)
                                        }
                                        builder.show()
                                    }
                                    else{
                                        if(textInputEditTextJoinStepOnePw.text.toString() == "") {
                                            val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                                setTitle("비밀번호 입력 오류")
                                                setMessage("비밀번호를 입력해주세요.")
                                                setNegativeButton("확인", null)
                                            }
                                            builder.show()
                                        }
                                        else if(textInputEditTextJoinStepOnePwCheck.text.toString() == "") {
                                            val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                                setTitle("비밀번호 입력 오류")
                                                setMessage("비밀번호를 확인해주세요.")
                                                setNegativeButton("확인", null)
                                            }
                                            builder.show()
                                        }
                                        else{
                                            if(textInputEditTextJoinStepOnePw.text.toString() != textInputEditTextJoinStepOnePwCheck.text.toString()){
                                                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                                    setTitle("비밀번호 입력 오류")
                                                    setMessage("비밀번호가 다릅니다.")
                                                    setNegativeButton("확인", null)
                                                }
                                                builder.show()
                                            }
                                            else {
                                                pw = textInputEditTextJoinStepOnePw.text.toString()
                                                //이메일 인증
                                                auth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener(loginMainActivity) {task ->
                                                    if(task.isSuccessful){
                                                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {sendTask ->
                                                            if(sendTask.isSuccessful)
                                                            {
                                                                buttonJoinStepOneAuth.text = "인증메일 전송됨"
                                                                buttonJoinStepOneAuth.isEnabled = false
                                                                buttonJoinStepOneAuthCheck.visibility = View.VISIBLE
                                                                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                                                    setTitle("인증메일 전송")
                                                                    setMessage("인증 메일이 전송되었습니다. 인증 후 인증완료 버튼을 눌러주세요.")
                                                                    setNegativeButton("확인", null)
                                                                }
                                                                builder.show()
                                                            }
                                                            else{
                                                            }
                                                        }
                                                    }
                                                    else{
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //인증 완료 했는지 체크
            buttonJoinStepOneAuthCheck.setOnClickListener {
                auth.signInWithEmailAndPassword(email,pw).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val user = Firebase.auth.currentUser
                        if (user != null) {
                            val isEmailVerified = user.isEmailVerified
                            if (isEmailVerified) {
                                // 이메일이 인증된 상태
                                loginMainActivity.checkEmail = 1
                                buttonJoinStepOneAuth.visibility = View.GONE
                                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                    setTitle("인증완료")
                                    setMessage("인증이 완료되었습니다.")
                                    setNegativeButton("확인", null)
                                }
                                builder.show()
                                buttonJoinStepOneAuthCheck.isEnabled = false
                                textInputEditTextJoinStepOneEmail.isEnabled = false
                                textInputEditTextJoinStepOnePw.isEnabled = false
                                textInputEditTextJoinStepOnePwCheck.isEnabled = false
                            } else {
                                // 이메일이 아직 인증되지 않은 상태
                                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                                    setTitle("인증오류")
                                    setMessage("인증메일을 확인하고 인증을 완료해주세요.")
                                    setNegativeButton("확인", null)
                                }
                                builder.show()
                            }
                        } else {
                            // 사용자가 로그인하지 않은 상태
                        }
                    }
                }
            }

            //다음버튼
            buttonJoinStepOneNext.setOnClickListener {
                if(loginMainActivity.checkEmail == 1){
                    if(checkBoxJoinStepOnePersonalInfoTerms.isChecked == false || checkBoxJoinStepOneServiceTerms.isChecked == false){
                        val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                            setTitle("약관 동의 필요")
                            setMessage("약관에 동의해주세요.")
                            setNegativeButton("확인", null)
                        }
                        builder.show()
                    }
                    else{
                        loginMainActivity.userEmail = email
                        loginMainActivity.userPw = pw
                        loginMainActivity.userAuth = "이메일"
                        loginMainActivity.replaceFragment(LoginMainActivity.JOIN_STEP_THREE_FRAGMENT, true, true, null)
                    }
                }
                else{
                    val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                        setTitle("이메일 인증 오류")
                        setMessage("이메일 인증을 진행해주세요.")
                        setNegativeButton("확인", null)
                    }
                    builder.show()
                }

            }

            //이용 약관 내용 보기
            textViewJoinStepOneServiceTerms.setOnClickListener {
                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                    setTitle("Trip Friend 서비스 이용 약관")
                    setMessage(R.string.service_terms_contents)
                    setNegativeButton("확인", null)
                }
                builder.show()
            }

            //개인 정보 약관 내용 보기
            textViewJoinStepOnePersonalInfoTerms.setOnClickListener {
                val builder= MaterialAlertDialogBuilder(loginMainActivity,R.style.DialogTheme).apply {
                    setTitle("개인 정보 수집 및 이용 동의")
                    setMessage(R.string.personal_info_terms_contents)
                    setNegativeButton("확인", null)
                }
                builder.show()
            }
        }

        return fragmentJoinStepOneBinding.root

    }
    //이메일 유효성 검사
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

    // Firebase Dynamic Link를 생성하여 동적 링크 URL을 반환
    private fun generateDynamicLink(email: String): Uri {
        // 이메일 주소를 확인할 동적 링크의 대상 페이지 URL
        val verifyEmailLink = "https://tripfriend.page.link/verify_email?email=$email"

        // Firebase Dynamic Links 생성 설정
        val dynamicLinkUri = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(verifyEmailLink))
            .setDomainUriPrefix("https://tripfriend.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.test.tripfriend")
                    .setMinimumVersion(1)
                    .build()
            )
            .buildDynamicLink()

        return dynamicLinkUri.uri
    }


    //뒤로가기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                loginMainActivity.checkEmail = 0
                loginMainActivity.removeFragment(LoginMainActivity.JOIN_STEP_TWO_FRAGMENT)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}

// Firebase Dynamic Links를 사용하여 동적 링크 생성
// val dynamicLinkUri = generateDynamicLink(email)
//                                                        val actionCodeSettings = ActionCodeSettings.newBuilder()
//                                                            .setUrl(dynamicLinkUri.toString())
//                                                            .setHandleCodeInApp(true)
//                                                            .setAndroidPackageName(
//                                                                "com.test.tripfriend",
//                                                                /* installIfNotAvailable */ false,
//                                                                /* minimumVersion */ null
//                                                            )
//                                                            .build()
//
//                                                        auth.sendSignInLinkToEmail(email, actionCodeSettings)
//                                                            .addOnCompleteListener { task ->
//                                                                if (task.isSuccessful) {
//                                                                    // 이메일 인증이 성공적으로 완료될 경우에 원하는 작업을 수행합니다.
//                                                                    loginMainActivity.checkEmail = 1
//                                                                    buttonJoinStepOneAuth.text = "인증완료"
//                                                                    buttonJoinStepOneAuth.isEnabled = false
//                                                                } else {
//                                                                    // 이메일 인증이 실패한 경우에 처리할 코드를 작성합니다.
//                                                                    Log.e(TAG, "이메일 링크 인증 실패: ${task.exception}")
//                                                                }
//                                                            }