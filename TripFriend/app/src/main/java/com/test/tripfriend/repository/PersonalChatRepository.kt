package com.test.tripfriend.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.test.tripfriend.dataclassmodel.PersonalChatRoom
import com.test.tripfriend.dataclassmodel.PersonalChatRoom2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PersonalChatRepository {
    //파이어스토어 인스턴스 객체
    private val db = FirebaseFirestore.getInstance()

    //문의하기 클릭시 채팅방을 생성하는 메서드
    fun inquiryToChatRoom(
        personalChatRoom: PersonalChatRoom,
        callback: (Task<DocumentReference>) -> Unit,
    ) {
        db.collection("PersonalChatRoom").add(personalChatRoom).addOnCompleteListener(callback)
    }

    //채팅방의 상대방 필요한 정보를 가져오는 메서드
    suspend fun getPersonalChatInfo(myEmail: String): QuerySnapshot {
        val infoRef = db.collection("User").whereEqualTo("userEmail", myEmail).get().await()

        return infoRef
    }

    //내가 속해있는 개인채팅방을 모두 불러오는 메서드
    suspend fun getMyPersonalChatRoom(myEmail: String): QuerySnapshot {
        val chatRoomRef = db.collection("PersonalChatRoom").where(
            Filter.or(
                Filter.equalTo("personalChatPostWriterEmail", myEmail),
                Filter.equalTo("personalChatRequesterEmail", myEmail)
            )
        ).get().await()

        return chatRoomRef
    }

    //가장 죄근 채팅 가져오기
    suspend fun getLastChat(documentId: String): QuerySnapshot {
        val lastChat =
            db.collection("PersonalChatRoom").document(documentId).collection("PersonalChatting")
                .orderBy("personalChatSendTimeStamp", Query.Direction.DESCENDING).limit(1).get()
                .await()

        return lastChat
    }


//    //채팅 가져오기
//    fun getLastChat(documentId:String){
//        val lastChat= db.collection("PersonalChatRoom").document(documentId).get()
//    }
}