package com.test.tripfriend.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.test.tripfriend.dataclassmodel.User

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
    }
}