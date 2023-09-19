package com.test.tripfriend.repository

import android.content.SharedPreferences
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.test.tripfriend.dataclassmodel.User
import com.test.tripfriend.dataclassmodel.UserLogin

class UserRepository {
    companion object {
        fun addUser(userClass: User, callback: (Task<DocumentReference>) -> Unit) {
            // Firestore 초기화
            val db = FirebaseFirestore.getInstance()

            // 사용자 정보 입력
            val user = HashMap<String, Any>()
            user["userAuthentication"] = userClass.userAuthentication
            user["userEmail"] = userClass.userEmail
            user["userPw"] = userClass.userPw
            user["userName"] = userClass.userName
            user["userNickname"] = userClass.userNickname
            user["userPhoneNum"] = userClass.userPhoneNum
            user["userMBTI"] = userClass.userMBTI
            user["userProfilePath"] = userClass.userProfilePath
            user["userFriendSpeed"] = userClass.userFriendSpeed
            user["userTripScore"] = userClass.userTripScore
            user["userTripCount"] = userClass.userTripCount
            user["userChatNotification"] = userClass.userChatNotification
            user["userPushNotification"] = userClass.userPushNotification

            // Firestore 컬렉션에 추가
            db.collection("User")
                .add(user)
                .addOnCompleteListener(callback)
        }

        fun getAllUser(callback: (Task<QuerySnapshot>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection("User")
                .get()
                .addOnCompleteListener(callback)
        }
        fun saveUserInfo(sharedPreferences: SharedPreferences, userClass: User, autoLogin:Boolean){
            val editor = sharedPreferences.edit()

            editor.putString("userAuthentication", userClass.userAuthentication)
            editor.putString("userEmail", userClass.userEmail)
            editor.putString("userPw", userClass.userPw)
            editor.putString("userNickname", userClass.userNickname)
            editor.putString("userName", userClass.userName)
            editor.putString("userPhoneNum", userClass.userPhoneNum)
            editor.putString("userMBTI", userClass.userMBTI)
            editor.putString("userProfilePath", userClass.userProfilePath)
            editor.putFloat("userFriendSpeed", userClass.userFriendSpeed.toFloat())
            editor.putFloat("userTripScore", userClass.userTripScore.toFloat())
            editor.putLong("userTripCount", userClass.userTripCount.toLong())
            editor.putBoolean("userChatNotification", userClass.userChatNotification)
            editor.putBoolean("userPushNotification", userClass.userPushNotification)
            editor.putBoolean("autoLogin",autoLogin)

            editor.apply()
        }
        fun checkUserInfo(sharedPreferences: SharedPreferences): Boolean{

            val userAuthentication = sharedPreferences.getString("userAuthentication", null)
            val userEmail = sharedPreferences.getString("userEmail", null)
            val userPw = sharedPreferences.getString("userPw", null)
            val userNickname = sharedPreferences.getString("userNickname", null)
            val userName = sharedPreferences.getString("userName", null)
            val userMBTI = sharedPreferences.getString("userMBTI", null)
            val userProfilePath = sharedPreferences.getString("userProfilePath", null)
            val userFriendSpeed = sharedPreferences.getFloat("userFriendSpeed", 0.0f)
            val userPhoneNum = sharedPreferences.getString("userPhoneNum", null)
            val userTripScore = sharedPreferences.getFloat("userTripScore", -1f)
            val userTripCount = sharedPreferences.getLong("userTripCount", -1L)
            val userChatNotification = sharedPreferences.getBoolean("userChatNotification", true)
            val userPushNotification = sharedPreferences.getBoolean("userPushNotification", true)
            val autoLogin = sharedPreferences.getBoolean("autoLogin", false)

            return !(userAuthentication == null || userEmail == null || userPw == null ||
                    userNickname == null || userName == null || userMBTI == null ||
                    userProfilePath == null || userPhoneNum == null || userTripScore == -1f ||
                    userTripCount == -1L)
        }

        //로그인 정보가 저장됐을 경우만
        fun getUserInfo(sharedPreferences: SharedPreferences) : UserLogin{
            val userAuthentication = sharedPreferences.getString("userAuthentication", null)
            val userEmail = sharedPreferences.getString("userEmail", null)
            val userPw = sharedPreferences.getString("userPw", null)
            val userNickname = sharedPreferences.getString("userNickname", null)
            val userName = sharedPreferences.getString("userName", null)
            val userMBTI = sharedPreferences.getString("userMBTI", null)
            val userProfilePath = sharedPreferences.getString("userProfilePath", null)
            val userFriendSpeed = sharedPreferences.getFloat("userFriendSpeed", 0.0f)
            val userPhoneNum = sharedPreferences.getString("userPhoneNum", null)
            val userTripScore = sharedPreferences.getFloat("userTripScore", -1f)
            val userTripCount = sharedPreferences.getLong("userTripCount", -1L)
            val userChatNotification = sharedPreferences.getBoolean("userChatNotification", true)
            val userPushNotification = sharedPreferences.getBoolean("userPushNotification", true)
            val autoLogin = sharedPreferences.getBoolean("autoLogin", false)

            val userClass = UserLogin(
                userAuthentication!!,
                userEmail!!,
                userPw!! ,
                userNickname!! ,
                userName!!,
                userPhoneNum!! ,
                userMBTI!!,
                userProfilePath = userProfilePath!!,
                userFriendSpeed = userFriendSpeed.toDouble()!!,
                userTripScore = userTripScore!!.toDouble(),
                userTripCount = userTripCount!!.toInt(),
                userChatNotification = userChatNotification!!,
                userPushNotification = userPushNotification!!,
                autoLogin
            )

            return userClass
        }

        fun resetUserInfo(sharedPreferences: SharedPreferences){
            //sharedreferences 값 삭제
            val editor = sharedPreferences.edit()

            editor.remove("userAuthentication")
            editor.remove("userEmail")
            editor.remove("userPw")
            editor.remove("userNickname")
            editor.remove("userName")
            editor.remove("userPhoneNum")
            editor.remove("userMBTI")
            editor.remove("userProfilePath")
            editor.remove("userFriendSpeed")
            editor.remove("userTripScore")
            editor.remove("userTripCount")
            editor.remove("userChatNotification")
            editor.remove("userPushNotification")
            editor.remove("autoLogin")

            editor.apply()
        }
    }
}
