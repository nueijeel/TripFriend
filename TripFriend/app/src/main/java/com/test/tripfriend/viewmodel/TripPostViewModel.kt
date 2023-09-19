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

class TripPostViewModel: ViewModel() {
    val tripPostRepository = TripPostRepository()

    val tripPostList = MutableLiveData<List<TripPost>>()

    private val _tripPost = MutableLiveData<TripPost>()
    val tripPost : LiveData<TripPost>
        get() = _tripPost

    fun getTripPostData() {
        val currentTripPostSnapshot =
            runBlocking { tripPostRepository.getDocumentData() }

        if(currentTripPostSnapshot != null) {
            val currentTripPost = currentTripPostSnapshot.toObjects(TripPost::class.java)

            tripPostList.value = currentTripPost
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