package com.test.tripfriend.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    // 동행글 이미지 url 가져오는 함수
    suspend fun getTripPostImage(tripPostImagePath : String) : Uri {
        val storage = Firebase.storage

        //인자로 전달된 ImagePath의 경로 형태 확인 필
        val fileRef = storage.reference.child(tripPostImagePath)
        Log.d("ㅁㅇtripPostImagePath", tripPostImagePath)
        return fileRef.downloadUrl.await()
    }

    // 동행글 삭제하는 함수
    fun deleteTripPostData(documentId: String){
        val firestore = Firebase.firestore

        firestore.collection("TripPost")
            .document(documentId)
            .delete()
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
    }
}