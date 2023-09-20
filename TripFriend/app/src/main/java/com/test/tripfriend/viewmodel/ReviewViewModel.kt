package com.test.tripfriend.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.test.tripfriend.dataclassmodel.ReviewContentState
import com.test.tripfriend.dataclassmodel.TripMemberInfo
import com.test.tripfriend.repository.ReviewRepository
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
    val saveState=MutableLiveData<Boolean>()

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

    fun saveToReview(reviewList: Array<ReviewContentState>) {
        for (review in reviewList) {
            runBlocking {
                reviewRepository.saveReviewToDB(review)
            }
        }
        saveState.value=true

    }

}