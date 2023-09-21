package com.test.tripfriend.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.test.tripfriend.dataclassmodel.TripPost
import kotlinx.coroutines.tasks.await

class TripPostRepository {
    val db = FirebaseFirestore.getInstance()

    // 문서 가져오기 메서드
    suspend fun getAllDocumentData(userEmail: String): QuerySnapshot {
        val docRef =
            db.collection("TripPost").whereEqualTo("tripPostWriterEmail", userEmail).get().await()

        return docRef
    }
    // 문서id로 접근해서 DB가져오기
    suspend fun getSelectDocumentData(documentId: String): DocumentSnapshot {
        val docRef = db.collection("TripPost").document(documentId).get().await()

        return docRef
    }
    // 동행글 이미지 url 가져오는 함수
    suspend fun getTripPostImage(tripPostImagePath: String): Uri {
        val storage = Firebase.storage

        //인자로 전달된 ImagePath의 경로 형태 확인 필
        val fileRef = storage.reference.child(tripPostImagePath)
        return fileRef.downloadUrl.await()
    }

    // 동행글 삭제하는 함수
    fun deleteTripPostData(documentId: String) {
        val firestore = Firebase.firestore

        firestore.collection("TripPost")
            .document(documentId)
            .delete()
    }

    // 좋아요 필드에 본인 이메일 있는 것만 가져오기
    suspend fun getTripPostLikedData(userEmail: String): QuerySnapshot {
        val docRef =
            db.collection("TripPost").whereArrayContains("tripPostLiked", userEmail).get().await()
        return docRef
    }
    // 좋아요 클릭시 해당 필드에 이메일 저장
    fun addLikedClick(documentId: String, userEmail: String) {
        db.collection("TripPost").document(documentId)
            .update("tripPostLiked", FieldValue.arrayUnion(userEmail))
    }
    // 좋아요 클릭시 해당 필드의 이메일 삭제
    fun deleteLikedClick(documentId: String, userEmail: String) {
        db.collection("TripPost").document(documentId)
            .update("tripPostLiked", FieldValue.arrayRemove(userEmail))

    }

    // 동행글 db에 업데이트하기
    fun updateTripPostData(documentId: String, tripPost: TripPost, callback1: (Task<Void>) -> Unit) {
        db.collection("TripPost").document(documentId).set(tripPost).addOnCompleteListener(callback1)
    }
    // 원래 있던 이미지 삭제
    fun deleteTripPostImage(fileDir : String, callback1: (Task<Void>) -> Unit) {
        val storage = FirebaseStorage.getInstance()

        val fileRef = storage.reference.child(fileDir)
        fileRef.delete().addOnCompleteListener(callback1)
    }
    // 이미지 업로드
    fun uploadTripPostImages(uploadUri : Uri, fileDir : String, callback1: (Task<UploadTask.TaskSnapshot>?) -> Unit) {
        val storage = FirebaseStorage.getInstance()

        if (uploadUri.toString().isNotEmpty()) {
            val imageRef = storage.reference.child(fileDir)
            imageRef.putFile(uploadUri).addOnCompleteListener(callback1)
        } else {
            callback1.invoke(null)
        }
    }

    //해당하는 동행글 데이터만 가져오는 메서드
    suspend fun getTargetUserTripPost(tripPostDocumentId : String) : DocumentSnapshot {
        return db.collection("TripPost")
            .document(tripPostDocumentId)
            .get()
            .await()
    }

    fun addTripMemberNickname(userNickname: String, tripPostDocumentId: String) {
        db.collection("TripPost")
            .document(tripPostDocumentId)
            .update("tripPostMemberList", FieldValue.arrayUnion(userNickname))
    }

    fun deleteTripMemberNickname(userNickname: String, tripPostDocumentId: String) {
        db.collection("TripPost")
            .document(tripPostDocumentId)
            .update("tripPostMemberList", FieldValue.arrayRemove(userNickname))
    }

    suspend fun findGroupChatIdByPostId(postId: String): DocumentSnapshot {
        val test = db.collection("GroupChatRoom").document(postId).get().await()

        return test
    }
}