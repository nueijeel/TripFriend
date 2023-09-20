package com.test.tripfriend.dataclassmodel

data class TripRequest(
    val tripRequestPostId : String = "",             //동행글 id (document id)
    val tripRequestWriterEmail : String = "",        //신청자 이메일
    val tripRequestReceiverEmail : String = "",      //동행글 주인(요청 받는 사람) 이메일
    val tripRequestContent : String = "",             //자기소개 내용
    val tripRequestState : String = ""               //수락 상태
)