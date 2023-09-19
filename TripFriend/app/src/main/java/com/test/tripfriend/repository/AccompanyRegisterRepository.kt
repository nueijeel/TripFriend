package com.test.tripfriend.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.test.tripfriend.dataclassmodel.PersonalChatting
import com.test.tripfriend.dataclassmodel.TripPost
import kotlinx.coroutines.tasks.await

class AccompanyRegisterRepository {
    // 파이어스토어 인스턴스 객체
    private val db = FirebaseFirestore.getInstance()

    // 동행글 db에 저장하기
    fun saveAccompanyToDB(tripPost: TripPost) {
        db.collection("TripPost").add(tripPost).addOnSuccessListener { documentReference ->
            // 데이터 추가 성공
            Log.d("qwer", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                // 데이터 추가 실패
                Log.w("qwer", "Error adding document", e)
            }
    }

    fun getPostIdx(callback1: (Task<QuerySnapshot>) -> Unit) {
        val collectionRef = db.collection("TripPost")

//        collectionRef.get().addOnCompleteListener(callback1)

        collectionRef.orderBy("tripPostIdx", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(callback1)
//        Log.d("qwer", "${collectionRef.orderBy("tripPostIdx", Query.Direction.DESCENDING).limit(1)}")
    }
}