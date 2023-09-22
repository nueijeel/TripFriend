package com.test.tripfriend.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.test.tripfriend.dataclassmodel.TripPost
import com.test.tripfriend.repository.HomeListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    val homeListRepository = HomeListRepository()

    val tripPostList = MutableLiveData<List<TripPost>>()

    // 홈 목록 가져오기
    fun getTripPostData() {

        val homePostInfoList= mutableListOf<DocumentSnapshot>()
        val resultList = mutableListOf<TripPost>()

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val currentTripPostSnapshot = async { homeListRepository.getDocumentData() }

            homePostInfoList.addAll(currentTripPostSnapshot.await().documents)

            withContext(Dispatchers.Main) {
                for(document in homePostInfoList) {
                    val tripPostObj = document.toObject(TripPost::class.java)

                    if (tripPostObj != null) {
                        tripPostObj.tripPostDocumentId = document.id
                        resultList.add(tripPostObj)
                    }
                }

            }

            withContext(Dispatchers.Main) {
                tripPostList.value = resultList
            }

            scope.cancel()

        }
    }

    fun getFilteredPostList(
        categoryArray: MutableList<String>,
        genderList: MutableList<Boolean>,
        dateList: IntArray
    ) {

        val filteredPostInfoList = mutableListOf<DocumentSnapshot>()


        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            var resultList = mutableListOf<TripPost>()
            val currentTripPostSnapshot = async { homeListRepository.getDocumentData() }

            filteredPostInfoList.addAll(currentTripPostSnapshot.await().documents)

            for(document in filteredPostInfoList) {
                val tripPostObj = document.toObject(TripPost::class.java)
                if (tripPostObj != null) {
                    tripPostObj.tripPostDocumentId = document.id
                    resultList.add(tripPostObj)
                }
            }

            // 카테고리 필터링
            if ( categoryArray.size != 0) {
                resultList = resultList.filter { tripPost ->
                    categoryArray.any { category ->
                        tripPost.tripPostTripCategory?.contains(category) ?: false
                    }
                }.toMutableList()
            }

            // 성별 필터링
            if(genderList[0] == true) {
                resultList = resultList.filter { tripPost ->
                    (tripPost.tripPostGender[0] == genderList[0])
                }.toMutableList()
            } else if (genderList[1] == true) {
                resultList = resultList.filter { tripPost ->
                    (tripPost.tripPostGender[1] == genderList[1])
                }.toMutableList()
            }

            // 시작 날짜 기준 필터링
            if (dateList[0] != 0) {
                resultList = resultList.filter { tripPost ->
                    (tripPost.tripPostDate?.get(0)?.toInt()!! >= dateList[0] && tripPost.tripPostDate[0]
                        .toInt() <= dateList[1])
                }.toMutableList()
            }

            withContext(Dispatchers.Main) {
                for(result in resultList) {
                    tripPostList.value = resultList
                }

            }

            scope.cancel()
        }
    }

}