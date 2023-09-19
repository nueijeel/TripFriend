package com.test.tripfriend.dataclassmodel

data class TripPost (
    val tripPostWriterEmail : String?=null,               //작성자이메일
    val tripPostTitle : String?=null,                     //제목
    val tripPostMemberList : List<String>?=null,          //참여자 이메일 리스트
    val tripPostMemberCount : Int?=null,                  //인원수
    val tripPostImage : String?=null,                     //사진
    val tripPostDate : List<Int>?=null,                   //날짜 idx 0-시작날짜, 1-마지막날짜//
    val tripPostLocationName : String?=null,              //지역명
    val tripPostLatitude : Double?=null,                  //위도
    val tripPostLongitude : Double?=null,                 //경도
    val tripPostLikedCount : Int?=null,                   //찜 개수
    val tripPostTripCategory : List<String>?=null,        //여행 카테고리 (3개)
    val tripPostHashTag : String?=null,                   //해시태그
    val tripPostContent : String?=null,                    //내용//
    val tripPostGender:String?=null
)