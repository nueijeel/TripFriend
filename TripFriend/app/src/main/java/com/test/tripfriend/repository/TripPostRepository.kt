package com.test.tripfriend.repository

import android.util.Log
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
}