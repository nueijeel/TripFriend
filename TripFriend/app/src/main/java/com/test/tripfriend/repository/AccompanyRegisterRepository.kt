package com.test.tripfriend.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.test.tripfriend.dataclassmodel.GroupChatRoom
import com.test.tripfriend.dataclassmodel.TripPost
import kotlinx.coroutines.tasks.await


class AccompanyRegisterRepository {
    // 파이어스토어 인스턴스 객체
    private val db = FirebaseFirestore.getInstance()

    // 동행글 db에 저장하기
    suspend fun saveAccompanyToDB(tripPost: TripPost, callback1: (Task<DocumentReference>) -> Unit) {
        db.collection("TripPost").add(tripPost).addOnCompleteListener(callback1).await()
//        db.collection("TripPost").add(tripPost)
//            .addOnCompleteListener { documentReference ->
//                if(documentReference.result.id.isNotEmpty()) {
//                    // 데이터 추가 성공
//                    Log.d("qwer", "DocumentSnapshot added with ID: ${documentReference.result.id}")
//                    Log.d("qwer", "DocumentRef: ${documentReference}")
//                } else {
//                    Log.d("qwer", "DocumentSnapshot added with ID: ${documentReference.result.id}")
//                }
//            }.addOnFailureListener { e ->
//                // 데이터 추가 실패
//                Log.w("qwer", "Error adding document", e)
//            }.await()
    }

    // 이미지 업로드
    fun uploadImages(uploadUri : Uri, fileDir : String, callback1: (Task<UploadTask.TaskSnapshot>?) -> Unit) {
        val storage = FirebaseStorage.getInstance()

        if (fileDir.isNotEmpty()) {
            val fileName = fileDir
            val imageRef = storage.reference.child(fileName)
            imageRef.putFile(uploadUri).addOnCompleteListener(callback1)
        } else {
            callback1.invoke(null)

        }

    }

    // postIdx 가져오기
    fun getPostIdx(callback1: (Task<QuerySnapshot>) -> Unit) {
        val collectionRef = db.collection("TripPost")

//        collectionRef.get().addOnCompleteListener(callback1)

        collectionRef.orderBy("tripPostIdx", Query.Direction.DESCENDING).limit(1).get()
            .addOnCompleteListener(callback1)
//        Log.d("qwer", "${collectionRef.orderBy("tripPostIdx", Query.Direction.DESCENDING).limit(1)}")
    }

    //단톡방 만들기
    suspend fun createGroupChatByPostTrip(groupChatRoom: GroupChatRoom,callback1: (Task<DocumentReference>) -> Unit){
        db.collection("GroupChatRoom").add(groupChatRoom).addOnCompleteListener(callback1).await()
    }

    //단톡방 아이디 삽입하기
    suspend fun addGroupChatIdToPostTrip(tripPostId:String,result:TripPost,callback1: (Task<Void>) -> Unit){

        db.collection("TripPost").document(tripPostId).set(result).addOnCompleteListener(callback1).await()
    }

}