package com.test.tripfriend.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.test.tripfriend.dataclassmodel.TripPost
import kotlinx.coroutines.tasks.await

class TripPostRepository {

    val db = FirebaseFirestore.getInstance()

    // 문서 가져오기 메서드
    suspend fun getDocumentData() : QuerySnapshot {
        val docRef = db.collection("TripPost").get().await()

        return docRef
    }

    //해당하는 동행글 데이터만 가져오는 메서드
    suspend fun getTargetUserTripPost(tripPostDocumentId : String) : DocumentSnapshot {
        return db.collection("TripPost")
            .document(tripPostDocumentId).get().await()
    }

    fun addTripMemberNickname(userNickname : String, tripPostDocumentId: String){
        db.collection("TripPost")
            .document(tripPostDocumentId)
            .update("tripPostMemberList", FieldValue.arrayUnion(userNickname))
            .addOnCompleteListener {
                Log.d("addTripMember", "success")
            }
            .addOnFailureListener {
                Log.d("addTripMember", "failure")
            }
    }
}