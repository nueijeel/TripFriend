package com.test.tripfriend.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.test.tripfriend.dataclassmodel.ReviewContentState
import com.test.tripfriend.dataclassmodel.TripMemberInfo
import com.test.tripfriend.dataclassmodel.TripReview
import com.test.tripfriend.dataclassmodel.User
import com.test.tripfriend.repository.ReviewRepository
import com.test.tripfriend.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class ReviewViewModel : ViewModel() {
    val reviewRepository = ReviewRepository()
    val userInfoList = MutableLiveData<MutableList<TripMemberInfo>>()
    val saveState = MutableLiveData<Boolean>()
    val saveState2 = MutableLiveData<Boolean>()

    //멤버의 정보를 불러오는 메서드(내 정보는 제외)
    fun getUserInfo(memberList: List<String>, myNickname: String) {
        val scope = CoroutineScope(Dispatchers.Default)

        scope.launch {
            val resultList = mutableListOf<TripMemberInfo>()
            for (memberIdx in 0 until memberList.size) {
                val users = mutableListOf<DocumentSnapshot>()
                if (memberList[memberIdx] == myNickname) {
                    continue
                } else {
                    val snapshot = async { reviewRepository.getUSerInfo(memberList[memberIdx]) }
                    users.addAll(snapshot.await().documents)

                    for (document in users) {
                        val data = document.toObject(TripMemberInfo::class.java)
                        if (data != null) {
                            resultList.add(data)
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                userInfoList.value = resultList
            }

            //코루틴 종료
            scope.cancel()
        }
    }

    //리뷰를 저장하는 메서드
    fun saveToReview(reviewList: Array<ReviewContentState>) {
        for (review in reviewList) {
            runBlocking {
                reviewRepository.saveReviewToDB(review)
            }
        }
        saveState.value = true

    }

    //리뷰들을 토대로 유저의 동행점수 등을 업데이트
    fun updateUserInfoByReview(reviewList: Array<ReviewContentState>, myEmail: String) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            //리뷰를 하나씩 꺼내서 유저 정보를 토대로 통신
            for (review in reviewList) {
                val userEmail = review.tripReviewTargetUserEmail
                val users = mutableListOf<DocumentSnapshot>()
                val documents = mutableListOf<DocumentSnapshot>()

                //상대방의 점수를 계산해서 저장하기위한 정보들 선언
                var reviewScore = 0
                var reviewCount = 0
                //이건 그냥 저장하면 됨
                var tripSpeed: Double = 0.0
                var userDocId: String? = null
                //리뷰 상대의 동행속도를 얻어오기 위해 스냅샷 가져오기
                val snapshot2 = async { UserRepository().getTargetUserData(userEmail) }
                users.addAll(snapshot2.await().documents)
                //해당 유저 탐색
                runBlocking {
                    for (user in users) {
                        val userObj = user.toObject<User>()
                        userDocId = user.id
                        if (userObj != null) {
                            //유저의 여행 속도값 초기화
                            tripSpeed = userObj.userFriendSpeed
                        }
                    }
                }
                //리뷰 상대의 모든 리뷰를 가져오기
                val snapshot = async { reviewRepository.getReviewByTargetEmail(userEmail) }
                documents.addAll(snapshot.await()!!.documents)
                //모든 리뷰 탐색
                runBlocking {
                    for (document in documents) {
                        val reviewObj = document.toObject<TripReview>()
                        reviewScore += reviewObj!!.tripReviewScore
                        tripSpeed += (reviewObj.tripReviewScore - 5) * 0.1
                        reviewCount++
                    }
                }
                //동행 점수
                val tripResult: Double = reviewScore.toDouble() / reviewCount.toDouble()
                //여행 속도
                val resultTripSpeed = tripSpeed

                // 업데이트할 데이터 준비
                val updates = hashMapOf(
                    "userTripScore" to tripResult,
                    "userFriendSpeed" to tripSpeed
                )
                runBlocking {
                    if (userDocId != null) {
                        UserRepository().updateUserTripScore(userDocId!!, updates)
                    }
                }
            }
            var myDocId: String = ""
            var myTripCount = 0
            runBlocking {
                val lastSnapshotList = mutableListOf<DocumentSnapshot>()
                val lastSnapshot = async { UserRepository().getTargetUserData(myEmail) }
                lastSnapshotList.addAll(lastSnapshot.await().documents)
                for (last in lastSnapshotList) {
                    myDocId = last.id
                    val myData = last.toObject<User>()
                    myTripCount = myData!!.userTripCount
                }
            }
            runBlocking {
                val updateMe = hashMapOf(
                    "userTripCount" to myTripCount+1,
                )
                UserRepository().updateUserTripCount(myDocId,updateMe)
            }
            withContext(Dispatchers.Main) {

                saveState2.value = true
            }
        }
    }
}

