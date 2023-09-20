package com.test.tripfriend.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class TripPostRepository {
    val db = FirebaseFirestore.getInstance()

    // 문서 가져오기 메서드
    suspend fun getAllDocumentData() : QuerySnapshot {
        val docRef = db.collection("TripPost").get().await()

        return docRef
    }
    
    // 문서id로 접근해서 DB가져오기
    suspend fun getSelectDocumentData(documentId: String): DocumentSnapshot {
        val docRef = db.collection("TripPost").document(documentId).get().await()

        return docRef
    }

    // 유저id로 DB 가져오기
    suspend fun getSelectUserData(userEmail: String): QuerySnapshot {
        val docRef = db.collection("User").whereEqualTo("userEmail", userEmail).get().await()

        return docRef
    }
}