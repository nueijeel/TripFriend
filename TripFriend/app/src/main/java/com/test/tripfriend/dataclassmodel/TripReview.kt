package com.test.tripfriend.dataclassmodel

data class TripReview(
    val tripReviewWriterEmail: String = "",         //동행 후기 작성자 이메일
    val tripReviewScore: Int = -1,               //여행 점수
    val tripReviewStyle: List<String> = emptyList(),         //여행 스타일
    val tripReviewPostId: String = "",              //동행글 id (document id)
    val tripReviewTargetUserEmail: String = "",      //후기 주인 이메일
)

class TripMemberInfo {
    val userEmail:String? = null
    val userNickname: String? = null
    val userProfilePath: String? = null
}

class ReviewContentState{
    var tripReviewScore:Int=0
    var tripReviewStyle:Array<String?> = Array(5){i->null}
    var tripReviewWriterEmail:String=""
    var tripReviewPostId:String=""
    var tripReviewTargetUserEmail:String=""
}