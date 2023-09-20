package com.test.tripfriend.repository

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class TripRequestRepository {

    val firestore = Firebase.firestore

    //유저가 받은 동행 요청 목록 가져오는 함수
    suspend fun getAllTripRequest(tripRequestWriterEmail : String) : QuerySnapshot{

        return firestore.collection("TripRequest")
            .whereEqualTo("tripRequestReceiverEmail", tripRequestWriterEmail)
            .whereEqualTo("tripRequestState", "대기중")
            .get().await()
    }

    suspend fun updateTripRequestAcceptState(tripRequestState : String, tripRequestDocumentId: String){
        firestore.collection("TripRequest")
            .document(tripRequestDocumentId)
            .update("tripRequestState", tripRequestState).await()
    }
}