package com.test.tripfriend.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class TripPostRepository {
    val db = FirebaseFirestore.getInstance()

    // 문서 가져오기 메서드
    fun getDocumentData(): Task<QuerySnapshot> {
        val docRef = db.collection("TripPost")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("ㅁㅇ", "${document.id} => ${document.data}")
                }
            }
        return docRef
    }
}