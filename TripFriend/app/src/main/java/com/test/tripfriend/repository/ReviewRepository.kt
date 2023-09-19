package com.test.tripfriend.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ReviewRepository {
    //파이어스토어 인스턴스 객체
    private val db = FirebaseFirestore.getInstance()

    //닉네임에 해당하는 유저 정보를 가져오는 메서드
    suspend fun getUSerInfo(memberNickname:String): QuerySnapshot {
      return  db.collection("User").whereEqualTo("userNickname",memberNickname).get().await()
    }

 

}