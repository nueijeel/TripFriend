package com.test.tripfriend.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.tripfriend.dataclassmodel.User
import com.test.tripfriend.repository.UserRepository
import kotlinx.coroutines.runBlocking

class UserViewModel : ViewModel() {
    //유저 레포지토리 객체
    val userRepository = UserRepository()

    //유저 뷰모델
    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    //유저 프로필 이미지 뷰모델
    private val _userProfileImage = MutableLiveData<Uri?>()
    val userProfileImage : MutableLiveData<Uri?>
        get() = _userProfileImage

    private val _userDocumentId = MutableLiveData<String>()
    val userDocumentId : LiveData<String>
        get() = _userDocumentId

    //_user 값 초기화
    fun getTargetUserData(targetUserEmail : String){
        //서버에서 유저 정보 가져옴
        val currentDocSnapshot =
            runBlocking {
                userRepository.getTargetUserData(targetUserEmail)
            }

        if(currentDocSnapshot != null){
            currentDocSnapshot.documents.forEach { documentSnapshot ->
                _userDocumentId.value = documentSnapshot.id
            }
            val currentUser = currentDocSnapshot.toObjects(User::class.java)
            currentUser.forEach { user ->
                _user.value = user
            }
        }
    }

    //_userProfileImage 값 초기화
    fun getTargetUserProfileImage(targetUserProfileImagePath : String){
        Log.d("aaaa","targetUserProfileImagePath1 = ${targetUserProfileImagePath}")
        if(targetUserProfileImagePath != "") {
            Log.d("aaaa","targetUserProfileImagePath2 = ${targetUserProfileImagePath}")
            val userProfileImageUri =
                runBlocking { userRepository.getTargetUserProfileImage(targetUserProfileImagePath) }

            if (userProfileImageUri != null) {
                _userProfileImage.value = userProfileImageUri!!
            }
        }
        else{
            Log.d("aaaa","targetUserProfileImagePath3 = ${targetUserProfileImagePath}")
            _userProfileImage.value = null
        }
    }
}