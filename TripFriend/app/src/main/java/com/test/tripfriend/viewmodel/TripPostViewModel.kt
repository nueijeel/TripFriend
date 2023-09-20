package com.test.tripfriend.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.test.tripfriend.dataclassmodel.TripPost
import com.test.tripfriend.dataclassmodel.TripRequest
import com.test.tripfriend.repository.TripPostRepository
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class TripPostViewModel: ViewModel() {
    val tripPostRepository = TripPostRepository()
    val tripPostInProgressList = MutableLiveData<List<TripPost>>()
    val tripPostPassList = MutableLiveData<List<TripPost>>()

    // 오늘 날짜
    val currentTime : Long = System.currentTimeMillis()
    val dataFormat = SimpleDateFormat("yyyyMMdd")
    val today = dataFormat.format(currentTime).toInt()

    private val _tripPost = MutableLiveData<TripPost>()
    val tripPost : LiveData<TripPost>
        get() = _tripPost

    fun getTripPostData() {
        val tripPostInfoList= mutableListOf<DocumentSnapshot>()
        val resultInProgressList = mutableListOf<TripPost>()
        val resultPassList = mutableListOf<TripPost>()

        val scope = CoroutineScope(Dispatchers.Default)

        scope.launch {
            val currentTripPostSnapshot = async { tripPostRepository.getDocumentData() }

            tripPostInfoList.addAll(currentTripPostSnapshot.await().documents)

            for(document in tripPostInfoList) {
                val data = document.toObject(TripPost::class.java)

                val tripPostObj = data

                var endDate = data?.tripPostDate

                // 참여중인 동행글
                if(today <= endDate!![1].toInt()) {
                    tripPostObj!!.tripPostDocumentId  = document.id

                    resultInProgressList.add(tripPostObj)
                } else { // 지난 동행글
                    tripPostObj!!.tripPostDocumentId  = document.id

                    resultPassList.add(tripPostObj)
                }
            }

            withContext(Dispatchers.Main) {
                tripPostInProgressList.value = resultInProgressList
                tripPostPassList.value = resultPassList
            }

            scope.cancel()
        }
    }

    fun getTargetUserTripPost(tripPostDocumentId : String){
        val tripPostDocumentSnapshot =
            runBlocking {
                tripPostRepository.getTargetUserTripPost(tripPostDocumentId)
            }

        if(tripPostDocumentSnapshot != null){
            val currentTripPost = tripPostDocumentSnapshot.toObject(TripPost::class.java)

            //현재 날짜 구하기
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val date : Long = currentDate.format(formatter).toLong()

            if(currentTripPost != null){
                if(currentTripPost.tripPostDate!![0].toLong() > date){
                    _tripPost.value = currentTripPost!!
                }
            }
        }
    }
}