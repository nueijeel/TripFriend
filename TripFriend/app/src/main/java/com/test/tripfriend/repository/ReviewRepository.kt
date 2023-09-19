package com.test.tripfriend.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.test.tripfriend.dataclassmodel.ReviewContentState
import kotlinx.coroutines.tasks.await
import javax.security.auth.callback.Callback

class ReviewRepository {
    //파이어스토어 인스턴스 객체
    private val db = FirebaseFirestore.getInstance()

    //닉네임에 해당하는 유저 정보를 가져오는 메서드
    suspend fun getUSerInfo(memberNickname:String): QuerySnapshot {
      return  db.collection("User").whereEqualTo("userNickname",memberNickname).get().await()
    }

    //리뷰데이터를 db에 저장하는 메서드
    fun saveReviewToDB(reviewContentState: ReviewContentState){
        db.collection("TripReview").add(reviewContentState)
    }

}